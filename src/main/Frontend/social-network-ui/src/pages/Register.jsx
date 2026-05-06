import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';

export default function Register() {
    const [formData, setFormData] = useState({ username: '', email: '', password: '' });
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            // Send the POST request to Spring Boot
            await api.post('/users/register', formData);
            alert('Registration successful! Please log in.');
            navigate('/login'); // Redirect to login page
        } catch (err) {
            // This will print the exact technical error to your browser console
            console.error("Full Error Details:", err);

            // This will show a more accurate message on the screen
            if (err.response) {
                setError(`Server rejected: ${err.response.status} - ${err.response.data}`);
            } else {
                setError('Network or CORS error. Check console.');
            }
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-gray-50 px-4">
            <div className="max-w-md w-full bg-white p-8 rounded-2xl shadow-xl border border-gray-100">
                <div className="text-center mb-8">
                    <h2 className="text-3xl font-bold text-gray-900 mb-2">Join the Network</h2>
                    <p className="text-gray-500">Create your account in seconds</p>
                </div>

                {error && (
                    <div className="mb-6 p-4 bg-red-50 border-l-4 border-red-500 text-red-700 text-sm">
                        {error}
                    </div>
                )}

                <form onSubmit={handleSubmit} className="space-y-5">
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">Username</label>
                        <input type="text" name="username" required onChange={handleChange}
                               className="w-full px-4 py-3 border border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-all bg-gray-50 focus:bg-white"
                               placeholder="johndoe" />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">Email</label>
                        <input type="email" name="email" required onChange={handleChange}
                               className="w-full px-4 py-3 border border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-all bg-gray-50 focus:bg-white"
                               placeholder="you@example.com" />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">Password</label>
                        <input type="password" name="password" required onChange={handleChange}
                               className="w-full px-4 py-3 border border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-all bg-gray-50 focus:bg-white"
                               placeholder="••••••••" />
                    </div>
                    <button type="submit"
                            className="w-full mt-4 bg-gray-900 hover:bg-black text-white font-semibold py-3 rounded-xl shadow-md transition-all duration-200 transform hover:-translate-y-0.5">
                        Create Account
                    </button>
                </form>

                <div className="mt-8 text-center text-sm text-gray-600">
                    Already have an account?{' '}
                    <span onClick={() => navigate('/login')} className="text-blue-600 hover:text-blue-800 font-bold cursor-pointer">
            Sign in
          </span>
                </div>
            </div>
        </div>
    );
}