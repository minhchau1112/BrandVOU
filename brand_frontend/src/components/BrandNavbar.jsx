import React, { useState, useEffect } from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import './BrandNavbar.css'; 

const BrandNavbar = () => {
    const [brandName, setName] = useState('');
    const [brandId, setId] = useState(null);
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        const storedBrandName = localStorage.getItem('brandName');
        const storedBrandId = localStorage.getItem('brandId');
        if (storedBrandName && storedBrandId) {
            setName(storedBrandName);
            setId(storedBrandId);
            setIsLoggedIn(true);
        }
    }, []);

    const handleLogout = () => {
        localStorage.clear();
        setIsLoggedIn(false);
    };

    const handleLogin = () => {
        navigate('/login');
    }

    const handleRegister = () => {
        navigate('/register');
    }

    return (
        <nav className="navbar">
            <div className="navbar-logo">
                <h1>VOU</h1>
                <span>Viral Marketing</span>
            </div>
            <ul className="navbar-links">
                <li>
                    <NavLink 
                        exact="true" 
                        to="/" 
                        className={({ isActive }) => (isActive ? 'active' : '')}
                    >
                        Home
                    </NavLink>
                </li>
                <li>
                    <NavLink 
                        to="/vouchers" 
                        className={({ isActive }) => (isActive ? 'active' : '')}
                    >
                        Vouchers
                    </NavLink>
                </li>
                <li>
                    <NavLink 
                        to="/budget-statistics" 
                        className={({ isActive }) => (isActive ? 'active' : '')}
                    >
                        Budget
                    </NavLink>
                </li>
            </ul>
            <div className="navbar-buttons">
                {isLoggedIn ? (
                    <div>
                        <span>{brandName}</span>
                        <button className="logout-btn" onClick={handleLogout}>
                            Logout
                        </button>
                    </div>
                ) : (
                    <div>
                        <button className="login-btn" onClick={handleLogin}>Login</button>
                        <button className="signup-btn" onClick={handleRegister}>Register</button>
                    </div>
                )}
            </div>
        </nav>
    );
};

export default BrandNavbar;