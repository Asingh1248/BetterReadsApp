#!/usr/bin/env python3
"""
Quick setup and run script for Vrinda AI
"""

import os
import sys
import subprocess
from pathlib import Path

def check_python_version():
    """Check if Python version is 3.8 or higher"""
    if sys.version_info < (3, 8):
        print("❌ Python 3.8 or higher is required")
        print(f"Current version: {sys.version}")
        return False
    print(f"✅ Python {sys.version.split()[0]} detected")
    return True

def check_env_file():
    """Check if .env file exists and has OpenAI API key"""
    env_file = Path(".env")
    if not env_file.exists():
        print("❌ .env file not found")
        print("Please create a .env file with your OpenAI API key")
        return False
    
    env_content = env_file.read_text()
    if "OPENAI_API_KEY=your_openai_api_key_here" in env_content:
        print("⚠️  Please update your OpenAI API key in the .env file")
        return False
    
    if "OPENAI_API_KEY=" in env_content:
        print("✅ .env file configured")
        return True
    else:
        print("❌ OPENAI_API_KEY not found in .env file")
        return False

def install_dependencies():
    """Install Python dependencies"""
    print("📦 Installing dependencies...")
    try:
        subprocess.run([sys.executable, "-m", "pip", "install", "-r", "requirements.txt"], 
                      check=True, capture_output=True)
        print("✅ Dependencies installed successfully")
        return True
    except subprocess.CalledProcessError as e:
        print(f"❌ Failed to install dependencies: {e}")
        return False

def run_server():
    """Run the FastAPI server"""
    print("🚀 Starting Vrinda AI server...")
    print("🌐 Access the chat at: http://localhost:8000")
    print("🔄 Press Ctrl+C to stop the server")
    print("-" * 50)
    
    try:
        subprocess.run([sys.executable, "app.py"])
    except KeyboardInterrupt:
        print("\n👋 Server stopped")

def main():
    """Main setup and run function"""
    print("🤖 Vrinda AI - Setup and Run")
    print("=" * 30)
    
    # Check Python version
    if not check_python_version():
        return
    
    # Check environment configuration
    if not check_env_file():
        print("\n📝 To set up your API key:")
        print("1. Get an API key from: https://platform.openai.com/api-keys")
        print("2. Edit the .env file and replace 'your_openai_api_key_here' with your actual API key")
        return
    
    # Install dependencies
    if not install_dependencies():
        return
    
    # Run the server
    run_server()

if __name__ == "__main__":
    main()