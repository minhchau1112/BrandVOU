import React from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import './BrandNavbar.css'; 
import {useAuth} from "../AuthProvider";
import Dropdown from 'react-bootstrap/Dropdown';

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
                        to="/items"
                        className={({ isActive }) => (isActive ? 'active' : '')}
                    >
                        Items
                    </NavLink>
                </li>

                <li>
                    <Dropdown>
                        <Dropdown.Toggle as="span" style={{ cursor: 'pointer', color: 'black'}}>
                            Statistic
                        </Dropdown.Toggle>

                        <Dropdown.Menu>
                            <Dropdown.Item href="/budget-statistics">Budget Statistic</Dropdown.Item>
                            <Dropdown.Item href="/voucher-statistics">Voucher Statistic</Dropdown.Item>
                        </Dropdown.Menu>
                    </Dropdown>
                </li>

            </ul>
            <div className="navbar-buttons">
                {auth.token.accessToken && auth.token.accessToken !== "" ? (
                    <div className="d-flex align-items-center" style={{ gap: '12px' }}>
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