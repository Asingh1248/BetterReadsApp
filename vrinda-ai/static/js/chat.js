class VrindaChat {
    constructor() {
        this.sessionId = this.generateSessionId();
        this.chatContainer = document.getElementById('chat-container');
        this.messageInput = document.getElementById('message-input');
        this.sendButton = document.getElementById('send-button');
        this.clearButton = document.getElementById('clear-button');
        this.typingIndicator = document.getElementById('typing-indicator');
        this.charCount = document.getElementById('char-count');
        this.statusIndicator = document.getElementById('status-indicator');
        
        this.isTyping = false;
        this.chatHistory = this.loadChatHistory();
        
        this.init();
    }

    init() {
        // Load existing chat history
        this.loadExistingMessages();
        
        // Event listeners
        this.sendButton.addEventListener('click', () => this.sendMessage());
        this.clearButton.addEventListener('click', () => this.clearChat());
        
        this.messageInput.addEventListener('keypress', (e) => {
            if (e.key === 'Enter' && !e.shiftKey) {
                e.preventDefault();
                this.sendMessage();
            }
        });
        
        this.messageInput.addEventListener('input', () => {
            this.updateCharCount();
        });
        
        // Auto-focus on input
        this.messageInput.focus();
        
        // Check connection status
        this.checkConnectionStatus();
    }

    generateSessionId() {
        return localStorage.getItem('vrinda_session_id') || 
               (() => {
                   const id = 'session_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9);
                   localStorage.setItem('vrinda_session_id', id);
                   return id;
               })();
    }

    updateCharCount() {
        const length = this.messageInput.value.length;
        this.charCount.textContent = `${length}/1000`;
        
        if (length > 900) {
            this.charCount.classList.add('text-red-500');
        } else {
            this.charCount.classList.remove('text-red-500');
        }
    }

    async sendMessage() {
        const message = this.messageInput.value.trim();
        if (!message || this.isTyping) return;

        // Add user message to UI
        this.addMessage('user', message);
        this.messageInput.value = '';
        this.updateCharCount();
        
        // Save to local storage
        this.saveChatHistory();
        
        // Show typing indicator
        this.showTypingIndicator();
        
        try {
            const response = await fetch('/chat', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    message: message,
                    session_id: this.sessionId
                })
            });

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const data = await response.json();
            
            // Hide typing indicator
            this.hideTypingIndicator();
            
            // Add AI response with typing effect
            await this.addMessageWithTypingEffect('assistant', data.response);
            
            // Save to local storage
            this.saveChatHistory();
            
        } catch (error) {
            console.error('Error sending message:', error);
            this.hideTypingIndicator();
            this.addMessage('assistant', 'Sorry, I encountered an error. Please try again or check your connection.');
            this.updateConnectionStatus(false);
        }
    }

    addMessage(sender, content, animate = true) {
        const messageDiv = document.createElement('div');
        messageDiv.className = `flex items-start space-x-3 ${animate ? 'chat-message' : ''}`;
        
        const isUser = sender === 'user';
        const timestamp = new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
        
        if (isUser) {
            messageDiv.innerHTML = `
                <div class="flex-1"></div>
                <div class="bg-blue-500 text-white p-4 rounded-lg shadow-sm max-w-md">
                    <div class="font-medium mb-1">You</div>
                    <div>${this.escapeHtml(content)}</div>
                    <div class="text-xs text-blue-100 mt-2">${timestamp}</div>
                </div>
                <div class="w-8 h-8 bg-blue-500 rounded-full flex items-center justify-center flex-shrink-0">
                    <i class="fas fa-user text-white text-sm"></i>
                </div>
            `;
        } else {
            messageDiv.innerHTML = `
                <div class="w-8 h-8 bg-gradient-to-r from-blue-500 to-purple-600 rounded-full flex items-center justify-center flex-shrink-0">
                    <i class="fas fa-robot text-white text-sm"></i>
                </div>
                <div class="bg-white p-4 rounded-lg shadow-sm max-w-md">
                    <div class="font-medium text-gray-800 mb-1">Vrinda</div>
                    <div class="text-gray-700" id="message-content-${Date.now()}">${this.escapeHtml(content)}</div>
                    <div class="text-xs text-gray-500 mt-2">${timestamp}</div>
                </div>
            `;
        }
        
        // Remove welcome message if this is the first user message
        if (isUser && this.chatContainer.children.length === 1) {
            this.chatContainer.innerHTML = '';
        }
        
        this.chatContainer.appendChild(messageDiv);
        this.scrollToBottom();
        
        // Add to chat history
        this.chatHistory.push({
            sender: sender,
            content: content,
            timestamp: new Date().toISOString()
        });
    }

    async addMessageWithTypingEffect(sender, content) {
        // First add an empty message
        this.addMessage(sender, '', false);
        
        // Get the content div
        const messageContentDiv = this.chatContainer.lastElementChild.querySelector('[id^="message-content-"]');
        
        // Type the message character by character
        await this.typeMessage(messageContentDiv, content);
        
        // Update chat history with complete message
        if (this.chatHistory.length > 0) {
            this.chatHistory[this.chatHistory.length - 1].content = content;
        }
    }

    async typeMessage(element, text, speed = 30) {
        element.innerHTML = '';
        element.classList.add('typewriter');
        
        for (let i = 0; i < text.length; i++) {
            element.innerHTML += text.charAt(i);
            this.scrollToBottom();
            await new Promise(resolve => setTimeout(resolve, speed));
        }
        
        element.classList.remove('typewriter');
    }

    showTypingIndicator() {
        this.isTyping = true;
        this.typingIndicator.classList.remove('hidden');
        this.sendButton.disabled = true;
        this.scrollToBottom();
    }

    hideTypingIndicator() {
        this.isTyping = false;
        this.typingIndicator.classList.add('hidden');
        this.sendButton.disabled = false;
    }

    async clearChat() {
        if (confirm('Are you sure you want to clear the chat history?')) {
            // Clear UI
            this.chatContainer.innerHTML = `
                <div class="flex items-start space-x-3 chat-message">
                    <div class="w-8 h-8 bg-gradient-to-r from-blue-500 to-purple-600 rounded-full flex items-center justify-center flex-shrink-0">
                        <i class="fas fa-robot text-white text-sm"></i>
                    </div>
                    <div class="bg-white p-4 rounded-lg shadow-sm max-w-md">
                        <div class="font-medium text-gray-800 mb-1">Vrinda</div>
                        <div class="text-gray-700">
                            Hello! I'm Vrinda, your personal AI assistant. I'm here to help you with questions, tasks, and conversations. How can I assist you today? 😊
                        </div>
                        <div class="text-xs text-gray-500 mt-2">Just now</div>
                    </div>
                </div>
            `;
            
            // Clear local storage
            this.chatHistory = [];
            this.saveChatHistory();
            
            // Clear server-side history
            try {
                await fetch(`/chat/history/${this.sessionId}`, {
                    method: 'DELETE'
                });
            } catch (error) {
                console.error('Error clearing server history:', error);
            }
            
            this.messageInput.focus();
        }
    }

    saveChatHistory() {
        localStorage.setItem(`vrinda_chat_${this.sessionId}`, JSON.stringify(this.chatHistory));
    }

    loadChatHistory() {
        const saved = localStorage.getItem(`vrinda_chat_${this.sessionId}`);
        return saved ? JSON.parse(saved) : [];
    }

    loadExistingMessages() {
        if (this.chatHistory.length > 0) {
            // Clear welcome message
            this.chatContainer.innerHTML = '';
            
            // Load messages from history
            this.chatHistory.forEach(msg => {
                this.addMessage(msg.sender, msg.content, false);
            });
        }
    }

    async checkConnectionStatus() {
        try {
            const response = await fetch('/health');
            const data = await response.json();
            this.updateConnectionStatus(data.status === 'healthy');
        } catch (error) {
            this.updateConnectionStatus(false);
        }
    }

    updateConnectionStatus(isOnline) {
        if (isOnline) {
            this.statusIndicator.className = 'w-3 h-3 bg-green-500 rounded-full';
            this.statusIndicator.nextElementSibling.textContent = 'Online';
        } else {
            this.statusIndicator.className = 'w-3 h-3 bg-red-500 rounded-full';
            this.statusIndicator.nextElementSibling.textContent = 'Offline';
        }
    }

    scrollToBottom() {
        this.chatContainer.scrollTop = this.chatContainer.scrollHeight;
    }

    escapeHtml(text) {
        const map = {
            '&': '&amp;',
            '<': '&lt;',
            '>': '&gt;',
            '"': '&quot;',
            "'": '&#039;'
        };
        return text.replace(/[&<>"']/g, m => map[m]);
    }
}

// Initialize the chat when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    new VrindaChat();
});

// Service worker for offline functionality (optional)
if ('serviceWorker' in navigator) {
    window.addEventListener('load', () => {
        navigator.serviceWorker.register('/static/js/sw.js')
            .catch(err => console.log('ServiceWorker registration failed: ', err));
    });
}