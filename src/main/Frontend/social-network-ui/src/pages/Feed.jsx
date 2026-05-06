import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import api from "../services/api.js";

export default function Feed()
{
    const[posts,setPosts]= useState([]);
    const[newContent,setNewContent]=useState('');
    const navigate=useNavigate();

    const fetchPosts=async()=>{
        try{
            const response =await api.get('/posts');
            setPosts(response.data);
        }
        catch(error)
        {
            console.error("Failed to fetch Posts",error);
            if(error.response && error.response.status===403)
            {
                handleLogout();
            }
        }
    };

    useEffect(()=>{
        fetchPosts();
    },[]);

    const handlePostSubmit=async (e)=>{
        e.preventDefault();
        if(!newContent.trim())return;

        try{
            await api.post('/posts',{content:newContent});
            setNewContent('');
            fetchPosts();
        }
        catch(error)
        {
            console.error("failed to create post:" ,error);
        }
};

    const handleLogout=()=>{
        localStorage.removeItem('token');
        navigate('/login');
        window.location.reload();
    };

    return (
        <div className="min-h-screen bg-gray-100 pb-12">
            {/* Top Navigation Bar */}
            <nav className="bg-white shadow-sm sticky top-0 z-50">
                <div className="max-w-2xl mx-auto px-4 py-3 flex justify-between items-center">
                    <h1 className="text-xl font-bold text-blue-600 tracking-tight">SocialNet</h1>
                    <div className="flex gap-3">
                        <button onClick={() => navigate('/network')} className="px-4 py-2 text-sm font-medium text-gray-600 hover:bg-gray-100 rounded-full transition">Network</button>
                        <button onClick={() => navigate('/chat')} className="px-4 py-2 text-sm font-medium text-gray-600 hover:bg-gray-100 rounded-full transition">Chat</button>
                        <button onClick={handleLogout} className="px-4 py-2 text-sm font-medium text-red-600 hover:bg-red-50 rounded-full transition">Logout</button>
                    </div>
                </div>
            </nav>

            <div className="max-w-2xl mx-auto mt-8 px-4">
                {/* Create Post Card */}
                <div className="bg-white p-5 rounded-2xl shadow-sm border border-gray-200 mb-8">
                    <form onSubmit={handlePostSubmit}>
            <textarea
                value={newContent}
                onChange={(e) => setNewContent(e.target.value)}
                placeholder="What's happening?"
                className="w-full resize-none outline-none text-lg text-gray-800 placeholder-gray-400 bg-transparent min-h-[100px]"
            />
                        <div className="flex justify-end pt-3 border-t border-gray-100 mt-2">
                            <button type="submit" disabled={!newContent.trim()}
                                    className="px-6 py-2 bg-blue-600 hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed text-white font-bold rounded-full shadow-sm transition-colors">
                                Post
                            </button>
                        </div>
                    </form>
                </div>

                {/* Timeline */}
                <div className="space-y-5">
                    {posts.length === 0 ? (
                        <div className="text-center py-12 text-gray-500 bg-white rounded-2xl border border-gray-200">
                            <p className="text-lg">Your feed is quiet.</p>
                            <p className="text-sm mt-1">Add some friends or make a post!</p>
                        </div>
                    ) : (
                        posts.map((post) => (
                            <div key={post.id} className="bg-white p-5 rounded-2xl shadow-sm border border-gray-200 hover:shadow-md transition-shadow">
                                <div className="flex justify-between items-start mb-3">
                                    <div className="flex items-center gap-3">
                                        <div className="w-10 h-10 bg-gradient-to-br from-blue-400 to-purple-500 rounded-full flex items-center justify-center text-white font-bold">
                                            {post.username.charAt(0).toUpperCase()}
                                        </div>
                                        <div>
                                            <h3 className="font-bold text-gray-900">@{post.username}</h3>
                                            <p className="text-xs text-gray-500">
                                                {new Date(post.createdAt).toLocaleDateString()} · {new Date(post.createdAt).toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'})}
                                            </p>
                                        </div>
                                    </div>
                                </div>
                                <p className="text-gray-800 whitespace-pre-wrap text-[15px] leading-relaxed ml-13">
                                    {post.content}
                                </p>
                            </div>
                        ))
                    )}
                </div>
            </div>
        </div>
    );
}