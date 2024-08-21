import React from 'react';
import './BrandNavbar.css'; 

const BrandNavbar = () => {
    return (
		<nav className="navbar">
		  <div className="navbar-logo">
			<h1>VOU</h1>
			<span>Viral Marketing</span>
		  </div>
		  <ul className="navbar-links">
			<li><a href="/">Home</a></li>
			<li><a href="/vouchers">Vouchers</a></li>
			<li><a href="/budget-statistics">Budget</a></li>
		  </ul>
		  <div className="navbar-buttons">
			<button className="login-btn">Login</button>
			<button className="signup-btn">Signup</button>
		  </div>
		</nav>
	  );
};

export default BrandNavbar;