# 🚀 Quick Start Guide - Vrinda AI

Get your Vrinda AI chatbot running in just 3 minutes!

## ⚡ Super Fast Setup

### Step 1: Get Your OpenAI API Key
1. Go to [OpenAI API Keys](https://platform.openai.com/api-keys)
2. Create a new API key
3. Copy the key (starts with `sk-...`)

### Step 2: Configure Environment
Edit the `.env` file:
```bash
OPENAI_API_KEY=sk-your-actual-api-key-here
```

### Step 3: Run the App
```bash
# Option 1: Use the automated setup script
python run.py

# Option 2: Manual setup
pip install -r requirements.txt
python app.py
```

### Step 4: Open Your Browser
Navigate to: **http://localhost:8000**

## 🎉 You're Done!

Start chatting with Vrinda! Try asking:
- "Hello, who are you?"
- "What can you help me with?"
- "Tell me a joke"
- "Explain quantum computing in simple terms"

## 📱 Features to Try

✅ **Chat Features:**
- Type messages and get AI responses
- See typing indicators while Vrinda thinks
- Watch the beautiful typewriter effect

✅ **Chat Management:**
- Click the trash icon to clear chat history
- Refresh the page - your chat history persists!
- Works on mobile, tablet, and desktop

✅ **Keyboard Shortcuts:**
- Press `Enter` to send messages
- Character counter shows remaining space

## 🔧 Having Issues?

**Port already in use?**
```bash
# Kill any existing servers
pkill -f "python app.py"
# Or use a different port
uvicorn app:app --port 8001
```

**API Key Error?**
- Make sure you copied the complete API key
- Check for extra spaces in the .env file
- Verify you have OpenAI credits

**Dependencies Error?**
```bash
# Create a virtual environment first
python -m venv venv
source venv/bin/activate  # On Windows: venv\Scripts\activate
pip install -r requirements.txt
```

## 🎯 Next Steps

1. **Customize Vrinda**: Edit the personality in `app.py`
2. **Change Styling**: Modify colors in `templates/index.html`
3. **Add Features**: Extend functionality in `static/js/chat.js`
4. **Deploy**: Use the Docker setup in README.md

---

**Enjoy chatting with Vrinda! 🤖💬**