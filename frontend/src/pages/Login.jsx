import {useState} from "react";
import {useNavigate} from "react-router-dom";
import api from "../services/api.js";

export default  function Login() {
    const [formData, setFormData] = useState({email: '', password: ''});
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleChange = (e) => {
        setFormData({...formData, [e.target.name]: e.target.value});
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const response = await api.post('/users/login', formData);

            const token = response.data;

            localStorage.setItem('token', token);

            navigate('/feed');

            window.location.reload();
        } catch (err) {
            setError('Invalid email or password');
        }
    };

    return (
        // This creates a full-screen, perfectly centered layout
        <div className="min-h-screen flex items-center justify-center bg-gray-100 px-4">

            {/* The main white card with a soft shadow and rounded corners */}
            <div className="max-w-md w-full bg-white p-8 rounded-xl shadow-lg border border-gray-100">

                <div className="text-center mb-8">
                    <h2 className="text-3xl font-bold text-gray-900 mb-2">Welcome Back</h2>
                    <p className="text-gray-500">Sign in to your social network</p>
                </div>

                {error && (
                    <div className="mb-4 p-3 bg-red-50 text-red-600 border border-red-200 rounded-lg text-sm text-center">
                        {error}
                    </div>
                )}

                <form onSubmit={handleSubmit} className="space-y-5">
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">Email Address</label>
                        <input
                            type="email"
                            name="email"
                            required
                            onChange={handleChange}
                            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-all"
                            placeholder="you@example.com"
                        />
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">Password</label>
                        <input
                            type="password"
                            name="password"
                            required
                            onChange={handleChange}
                            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-all"
                            placeholder="••••••••"
                        />
                    </div>

                    <button
                        type="submit"
                        className="w-full bg-blue-600 hover:bg-blue-700 text-white font-semibold py-2.5 rounded-lg shadow-md transition-colors duration-200"
                    >
                        Sign In
                    </button>
                </form>

                <div className="mt-6 text-center text-sm text-gray-600">
                    Don't have an account?{' '}
                    <span
                        onClick={() => navigate('/register')}
                        className="text-blue-600 hover:text-blue-800 font-semibold cursor-pointer"
                    >
            Register here
          </span>
                </div>
            </div>
        </div>
    );
}