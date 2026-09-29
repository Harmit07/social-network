import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';

export default function Network() {
    const [receiverEmail, setReceiverEmail] = useState('');
    const [pendingRequests, setPendingRequests] = useState([]);
    const [feedbackMsg, setFeedbackMsg] = useState('');
    const navigate = useNavigate();

    // 1. Fetch pending requests when the page loads
    const fetchPendingRequests = async () => {
        try {
            const response = await api.get('/friends/pending');
            setPendingRequests(response.data);
        } catch (error) {
            console.error('Failed to fetch pending requests', error);
        }
    };

    useEffect(() => {
        fetchPendingRequests();
    }, []);

    // 2. Handle sending a new friend request
    const handleSendRequest = async (e) => {
        e.preventDefault();
        if (!receiverEmail.trim()) return;

        try {
            const response = await api.post('/friends/request', {
                "receiverEmail": receiverEmail
            });
            setFeedbackMsg(response.data); // Display success message from Spring Boot
            setReceiverEmail(''); // Clear the input
        } catch (error) {
            // Display the error message sent by the backend (e.g., "Cannot send to yourself")
            setFeedbackMsg(error.response?.data?.message || 'Failed to send friend request.');
        }

        // Clear the message after 3 seconds
        setTimeout(() => setFeedbackMsg(''), 3000);
    };

    // 3. Handle accepting a request
    const handleAccept = async (connectionId) => {
        try {
            await api.put(`/friends/accept/${connectionId}`);
            setFeedbackMsg('Friend request accepted!');
            fetchPendingRequests(); // Refresh the list to remove the accepted request
        } catch (error) {
            setFeedbackMsg('Failed to accept request.');
        }

        setTimeout(() => setFeedbackMsg(''), 3000);
    };

    return (
        <div className="min-h-screen bg-gray-100">
            <nav className="bg-white shadow-sm sticky top-0 z-50">
                <div className="max-w-2xl mx-auto px-4 py-3 flex items-center">
                    <button onClick={() => navigate('/feed')} className="text-gray-500 hover:text-gray-800 font-medium mr-4 flex items-center gap-1">
                        ← Back
                    </button>
                    <h1 className="text-xl font-bold text-gray-900">My Network</h1>
                </div>
            </nav>

            <div className="max-w-2xl mx-auto mt-8 px-4 space-y-6 pb-12">

                {feedbackMsg && (
                    <div className="p-4 bg-blue-50 text-blue-700 rounded-xl border border-blue-100 text-sm font-medium animate-pulse">
                        {feedbackMsg}
                    </div>
                )}

                {/* Add Friend Card */}
                <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-200">
                    <h3 className="text-lg font-bold text-gray-900 mb-2">Grow your network</h3>
                    <p className="text-sm text-gray-500 mb-4">Invite someone to see your posts and chat.</p>
                    <form onSubmit={handleSendRequest} className="flex gap-3">
                        <input type="email" placeholder="friend@email.com" value={receiverEmail} onChange={(e) => setReceiverEmail(e.target.value)} required
                               className="flex-grow px-4 py-2 border border-gray-300 rounded-xl focus:ring-2 focus:ring-blue-500 outline-none transition-all bg-gray-50" />
                        <button type="submit" className="px-6 py-2 bg-gray-900 hover:bg-black text-white font-semibold rounded-xl transition-colors">
                            Send Invite
                        </button>
                    </form>
                </div>

                {/* Pending Requests List */}
                <div>
                    <h3 className="text-lg font-bold text-gray-900 mb-4 px-1">Pending Invitations</h3>
                    {pendingRequests.length === 0 ? (
                        <div className="bg-transparent border-2 border-dashed border-gray-300 rounded-2xl p-8 text-center text-gray-500">
                            You are all caught up! No pending requests.
                        </div>
                    ) : (
                        <div className="space-y-3">
                            {pendingRequests.map((request) => (
                                <div key={request.connectionId} className="flex justify-between items-center bg-white p-4 rounded-xl shadow-sm border border-gray-200">
                                    <div className="flex items-center gap-3">
                                        <div className="w-10 h-10 bg-gray-200 rounded-full flex items-center justify-center text-gray-600 font-bold">
                                            {request.requesterEmail.charAt(0).toUpperCase()}
                                        </div>
                                        <div>
                                            <p className="font-semibold text-gray-900">{request.requesterEmail}</p>
                                            <p className="text-xs text-gray-500">Wants to connect</p>
                                        </div>
                                    </div>
                                    <button onClick={() => handleAccept(request.connectionId)}
                                            className="px-5 py-2 bg-blue-600 hover:bg-blue-700 text-white text-sm font-semibold rounded-full transition-colors shadow-sm">
                                        Accept
                                    </button>
                                </div>
                            ))}
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}