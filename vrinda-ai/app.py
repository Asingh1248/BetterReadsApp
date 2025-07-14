from fastapi import FastAPI, HTTPException, Request
from fastapi.staticfiles import StaticFiles
from fastapi.templating import Jinja2Templates
from fastapi.responses import HTMLResponse, StreamingResponse
from pydantic import BaseModel
import openai
import os
import json
import asyncio
from datetime import datetime
from typing import List, Dict
from dotenv import load_dotenv

# Load environment variables
load_dotenv()

app = FastAPI(title="Vrinda AI Chatbot", description="A personalized AI chatbot powered by OpenAI")

# Mount static files
app.mount("/static", StaticFiles(directory="static"), name="static")

# Setup templates
templates = Jinja2Templates(directory="templates")

# Initialize OpenAI client
openai.api_key = os.getenv("OPENAI_API_KEY")

# In-memory storage for chat history (you can replace with database)
chat_histories: Dict[str, List[Dict]] = {}

class ChatMessage(BaseModel):
    message: str
    session_id: str = "default"

class ChatResponse(BaseModel):
    response: str
    timestamp: str

@app.get("/", response_class=HTMLResponse)
async def read_root(request: Request):
    """Serve the main chat interface"""
    return templates.TemplateResponse("index.html", {"request": request})

@app.post("/chat", response_model=ChatResponse)
async def chat_with_vrinda(chat_message: ChatMessage):
    """Handle chat messages and get responses from OpenAI"""
    try:
        # Initialize session history if it doesn't exist
        if chat_message.session_id not in chat_histories:
            chat_histories[chat_message.session_id] = [
                {
                    "role": "system",
                    "content": """You are Vrinda, a friendly and helpful AI assistant. You are knowledgeable, empathetic, and always ready to help users with their questions and tasks. 
                    Always introduce yourself as Vrinda on the first interaction and maintain a warm, professional tone throughout the conversation.
                    Keep your responses helpful, clear, and engaging."""
                }
            ]
        
        # Add user message to history
        chat_histories[chat_message.session_id].append({
            "role": "user",
            "content": chat_message.message
        })
        
        # Get response from OpenAI
        response = await get_openai_response(chat_histories[chat_message.session_id])
        
        # Add assistant response to history
        chat_histories[chat_message.session_id].append({
            "role": "assistant",
            "content": response
        })
        
        return ChatResponse(
            response=response,
            timestamp=datetime.now().isoformat()
        )
        
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error processing chat: {str(e)}")

async def get_openai_response(messages: List[Dict]) -> str:
    """Get response from OpenAI API"""
    try:
        client = openai.OpenAI(api_key=os.getenv("OPENAI_API_KEY"))
        
        response = client.chat.completions.create(
            model="gpt-3.5-turbo",  # You can change this to gpt-4 if you have access
            messages=messages,
            max_tokens=1000,
            temperature=0.7
        )
        
        return response.choices[0].message.content
        
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"OpenAI API error: {str(e)}")

@app.get("/chat/history/{session_id}")
async def get_chat_history(session_id: str):
    """Get chat history for a session"""
    if session_id in chat_histories:
        # Filter out system messages for display
        user_messages = [msg for msg in chat_histories[session_id] if msg["role"] != "system"]
        return {"history": user_messages}
    return {"history": []}

@app.delete("/chat/history/{session_id}")
async def clear_chat_history(session_id: str):
    """Clear chat history for a session"""
    if session_id in chat_histories:
        # Keep only the system message
        system_msg = next((msg for msg in chat_histories[session_id] if msg["role"] == "system"), None)
        chat_histories[session_id] = [system_msg] if system_msg else []
    return {"message": "Chat history cleared"}

@app.get("/health")
async def health_check():
    """Health check endpoint"""
    return {"status": "healthy", "service": "Vrinda AI Chatbot"}

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000, reload=True)