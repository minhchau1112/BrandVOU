import React from 'react';
import { NavLink } from 'react-router-dom';
import './BrandNavbar.css'; 

const BrandNavbar = () => {
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
                <button className="login-btn">Login</button>
                <button className="signup-btn">Signup</button>
            </div>
        </nav>
    );
};

export default BrandNavbar;