# 🤖 Vrinda AI - Personal AI Assistant

A modern, responsive web-based chatbot powered by OpenAI's GPT models. Built with FastAPI backend and vanilla JavaScript frontend with TailwindCSS styling.

![Vrinda AI Demo](https://img.shields.io/badge/AI-Powered-blue.svg)
![FastAPI](https://img.shields.io/badge/FastAPI-0.104.1-green.svg)
![OpenAI](https://img.shields.io/badge/OpenAI-GPT--3.5--turbo-orange.svg)

## ✨ Features

- 💬 **ChatGPT-like Interface**: Clean, modern chat UI with real-time messaging
- 🎭 **Personalized AI**: Vrinda has a unique personality and introduces herself
- ⚡ **Real-time Responses**: Fast API communication with typing indicators
- 💾 **Chat History**: Persistent chat history stored locally and server-side
- 📱 **Responsive Design**: Works perfectly on desktop, tablet, and mobile
- 🎨 **Typing Effects**: Beautiful typewriter effect for AI responses
- 🌙 **Dark Mode Support**: Automatic dark mode based on system preferences
- ♿ **Accessibility**: Screen reader friendly and keyboard navigation
- 🔄 **Offline Support**: Basic offline functionality with service worker
- 🧹 **Chat Management**: Clear chat history and session management

## 🏗️ Project Structure

```
vrinda-ai/
├── app.py                 # FastAPI backend application
├── requirements.txt       # Python dependencies
├── .env                  # Environment variables (API keys)
├── templates/
│   └── index.html        # Main chat interface
├── static/
│   ├── css/
│   │   └── style.css     # Custom styles
│   └── js/
│       ├── chat.js       # Chat functionality
│       └── sw.js         # Service worker
└── README.md             # This file
```

## 🚀 Quick Start

### Prerequisites

- Python 3.8 or higher
- OpenAI API key ([Get one here](https://platform.openai.com/api-keys))

### 1. Clone and Setup

```bash
# Navigate to the project directory
cd vrinda-ai

# Create a virtual environment
python -m venv venv

# Activate virtual environment
# On Windows:
venv\Scripts\activate
# On macOS/Linux:
source venv/bin/activate

# Install dependencies
pip install -r requirements.txt
```

### 2. Configure Environment

Edit the `.env` file and add your OpenAI API key:

```env
# OpenAI Configuration
OPENAI_API_KEY=sk-your-openai-api-key-here

# FastAPI Configuration
DEBUG=True
HOST=0.0.0.0
PORT=8000
```

### 3. Run the Application

```bash
# Start the FastAPI server
python app.py
```

Or use uvicorn directly:

```bash
uvicorn app:app --host 0.0.0.0 --port 8000 --reload
```

### 4. Access the Chat

Open your browser and navigate to:
```
http://localhost:8000
```

## 🔧 Configuration

### OpenAI Model Selection

You can change the AI model in `app.py`:

```python
# Change from gpt-3.5-turbo to gpt-4 (if you have access)
response = client.chat.completions.create(
    model="gpt-4",  # or "gpt-3.5-turbo"
    messages=messages,
    max_tokens=1000,
    temperature=0.7
)
```

### Customizing Vrinda's Personality

Edit the system message in `app.py`:

```python
{
    "role": "system",
    "content": """You are Vrinda, a friendly and helpful AI assistant. 
    [Customize this message to change Vrinda's personality]"""
}
```

## 📚 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/` | Serve the chat interface |
| POST | `/chat` | Send message and get AI response |
| GET | `/chat/history/{session_id}` | Get chat history for session |
| DELETE | `/chat/history/{session_id}` | Clear chat history for session |
| GET | `/health` | Health check endpoint |

## 💡 Features in Detail

### Chat History Management
- **Local Storage**: Chat history is saved in browser's localStorage
- **Session Management**: Each browser gets a unique session ID
- **Server-side Storage**: Chat context maintained on server for AI continuity
- **Clear Function**: One-click chat history clearing

### Responsive Design
- **Mobile-first**: Optimized for mobile devices
- **Tablet Support**: Perfect layout for tablets
- **Desktop Experience**: Full-featured desktop interface

### Accessibility Features
- **Keyboard Navigation**: Full keyboard support
- **Screen Reader**: ARIA labels and semantic HTML
- **High Contrast**: Support for high contrast mode
- **Reduced Motion**: Respects user's motion preferences

### Performance Optimizations
- **Service Worker**: Caches static assets for faster loading
- **Lazy Loading**: Efficient resource loading
- **Minimal Dependencies**: Fast loading times

## 🔒 Security Considerations

- **API Key Protection**: Store API keys in environment variables
- **Input Validation**: Server-side input validation and sanitization
- **Rate Limiting**: Consider implementing rate limiting for production
- **HTTPS**: Use HTTPS in production environments

## 🚀 Production Deployment

### Using Docker (Recommended)

Create a `Dockerfile`:

```dockerfile
FROM python:3.9-slim

WORKDIR /app

COPY requirements.txt .
RUN pip install -r requirements.txt

COPY . .

EXPOSE 8000

CMD ["uvicorn", "app:app", "--host", "0.0.0.0", "--port", "8000"]
```

### Using Heroku

```bash
# Install Heroku CLI and login
heroku create your-vrinda-ai-app

# Set environment variables
heroku config:set OPENAI_API_KEY=your-api-key-here

# Deploy
git push heroku main
```

### Using Railway, Render, or Vercel

Each platform supports FastAPI deployments with minimal configuration.

## 🛠️ Development

### Adding New Features

1. **Backend**: Add new endpoints in `app.py`
2. **Frontend**: Extend functionality in `static/js/chat.js`
3. **Styling**: Add custom styles in `static/css/style.css`

### Common Customizations

- **Change Colors**: Modify TailwindCSS classes in `templates/index.html`
- **Add Voice**: Integrate Web Speech API
- **File Upload**: Add file upload functionality
- **Multi-language**: Add language selection

## 🐛 Troubleshooting

### Common Issues

1. **"Import fastapi could not be resolved"**
   - Solution: Install dependencies with `pip install -r requirements.txt`

2. **"OpenAI API error"**
   - Check your API key in `.env` file
   - Verify you have sufficient OpenAI credits

3. **"404 Not Found" for static files**
   - Ensure the `static` folder structure is correct
   - Check file paths in templates

4. **Chat history not saving**
   - Check browser localStorage permissions
   - Verify JavaScript is enabled

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📞 Support

If you have any questions or issues, please:

1. Check the troubleshooting section
2. Search existing GitHub issues
3. Create a new issue with detailed information

## 🙏 Acknowledgments

- [OpenAI](https://openai.com/) for the GPT models
- [FastAPI](https://fastapi.tiangolo.com/) for the excellent web framework
- [TailwindCSS](https://tailwindcss.com/) for the utility-first CSS framework
- [Font Awesome](https://fontawesome.com/) for the beautiful icons

---

**Happy Chatting with Vrinda AI! 🎉**