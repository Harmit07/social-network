import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import { jwtDecode } from 'jwt-decode';

export default function Chat() {
    const [messages, setMessages] = useState([]);
    const [messageInput, setMessageInput] = useState('');
    const [stompClient, setStompClient] = useState(null);
    const [currentUser, setCurrentUser] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        // 1. Add a flag to track if the component is currently on the screen
        let isActive = true;

        const token = localStorage.getItem('token');
        let email = 'Unknown User';
        if (token) {
            const decoded = jwtDecode(token);
            email = decoded.sub;
            setCurrentUser(email);
        } else {
            navigate('/login');
        }

        const socket = new SockJS('http://localhost:8080/ws');
        const client = Stomp.over(socket);
        client.debug = () => {};

        client.connect({}, () => {
            // 2. THE FIX: If the component unmounted while we were waiting to connect,
            // kill this ghost connection immediately and stop running the code!
            if (!isActive) {
                client.disconnect();
                return;
            }

            console.log('Connected to WebSocket Tunnel!');

            client.subscribe('/topic/public', (payload) => {
                const receivedMessage = JSON.parse(payload.body);
                setMessages((prevMessages) => [...prevMessages, receivedMessage]);
            });

            client.send(
                '/app/chat.addUser',
                {},
                JSON.stringify({ sender: email, type: 'JOIN' })
            );
        });

        setStompClient(client);

        return () => {
            // 3. Mark the component as unmounted so any pending connections know to abort
            isActive = false;

            if (client && client.connected) {
                client.disconnect();
            }
        };
    }, [navigate]);

    // Handle sending a new message
    const sendMessage = (e) => {
        e.preventDefault();
        if (messageInput.trim() && stompClient) {
            const chatMessage = {
                sender: currentUser,
                content: messageInput,
                type: 'CHAT',
            };

            // Send the message through the tunnel to Spring Boot
            stompClient.send('/app/chat.sendMessage', {}, JSON.stringify(chatMessage));
            setMessageInput(''); // Clear the input box
        }
    };

    return (
        <div className="min-h-screen bg-gray-100 flex flex-col items-center pt-8 px-4">
            <div className="w-full max-w-2xl bg-white rounded-2xl shadow-xl border border-gray-200 overflow-hidden flex flex-col h-[80vh]">

                {/* Chat Header */}
                <div className="bg-white px-6 py-4 border-b border-gray-100 flex justify-between items-center shadow-sm z-10">
                    <div>
                        <h2 className="text-xl font-bold text-gray-900">Global Lounge</h2>
                        <p className="text-xs text-green-500 flex items-center gap-1">
                            <span className="w-2 h-2 bg-green-500 rounded-full animate-pulse"></span> Live connection
                        </p>
                    </div>
                    <button onClick={() => navigate('/feed')} className="text-gray-500 hover:bg-gray-100 px-3 py-1.5 rounded-lg text-sm font-medium transition-colors">
                        Exit Chat
                    </button>
                </div>

                {/* Scrollable Message Area */}
                <div className="flex-grow p-6 overflow-y-auto bg-gray-50 flex flex-col gap-4">
                    {messages.map((msg, index) => {
                        const isMe = msg.sender === currentUser;

                        if (msg.type === 'JOIN') {
                            return (
                                <div key={index} className="text-center my-2">
                  <span className="bg-gray-200 text-gray-500 text-xs px-3 py-1 rounded-full font-medium">
                    {msg.sender} joined the room
                  </span>
                                </div>
                            );
                        }

                        return (
                            <div key={index} className={`flex flex-col ${isMe ? 'items-end' : 'items-start'}`}>
                                <span className="text-[10px] text-gray-400 mb-1 ml-1 mr-1">{msg.sender}</span>
                                <div className={`max-w-[75%] px-4 py-2.5 rounded-2xl text-[15px] shadow-sm ${
                                    isMe
                                        ? 'bg-blue-600 text-white rounded-br-sm'
                                        : 'bg-white border border-gray-200 text-gray-800 rounded-bl-sm'
                                }`}>
                                    {msg.content}
                                </div>
                            </div>
                        );
                    })}
                </div>

                {/* Input Area */}
                <div className="p-4 bg-white border-t border-gray-100">
                    <form onSubmit={sendMessage} className="flex gap-2 relative">
                        <input
                            type="text"
                            value={messageInput}
                            onChange={(e) => setMessageInput(e.target.value)}
                            placeholder="Type your message..."
                            className="flex-grow bg-gray-100 border-none px-5 py-3 rounded-full focus:ring-2 focus:ring-blue-500 outline-none pr-24"
                            autoComplete="off"
                        />
                        <button type="submit" disabled={!messageInput.trim()}
                                className="absolute right-1 top-1 bottom-1 px-5 bg-blue-600 hover:bg-blue-700 disabled:opacity-50 text-white font-semibold rounded-full transition-colors">
                            Send
                        </button>
                    </form>
                </div>

            </div>
        </div>
    );
}