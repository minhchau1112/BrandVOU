import React, { useState, useEffect } from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import './BrandNavbar.css'; 
import {useAuth} from "../AuthProvider";

const BrandNavbar = () => {
    const navigate = useNavigate();

    const auth = useAuth();

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
                {auth.token.accessToken ? (
                    <div>
                        <span>{auth.brand.name}</span>
                        <button className="logout-btn" onClick={() => auth.logOut(auth.token)}>
                            Logout
                        </button>
                    </div>
                ) : (
                    <div className="d-flex" style={{ gap: '12px' }}>
                        <button className="login-btn" onClick={handleLogin}>Login</button>
                        <button className="signup-btn" onClick={handleRegister}>Register</button>
                    </div>
                )}
            </div>
        </nav>
    );
};

export default BrandNavbar;