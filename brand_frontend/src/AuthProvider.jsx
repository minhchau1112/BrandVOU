import React, { createContext, useContext, useState, useEffect, useCallback } from 'react';
import { useNavigate } from "react-router-dom";
import AuthService from "./services/AuthService";
import { HttpStatusCode } from "axios";

const AuthContext = createContext();

const AuthProvider = ({ children }) => {
    const storedBrand = JSON.parse(localStorage.getItem("brand")) || { id: "", name: "" };
    const storedToken = JSON.parse(localStorage.getItem("token")) || { accessToken: "", refreshToken: "", accessTokenExpiresAt: "" };
    const [brand, setBrand] = useState(storedBrand);
    const [token, setToken] = useState(storedToken);
    const navigate = useNavigate();

    const logOut = useCallback(async (data) => {
        const requestBody = JSON.stringify(data);
        try {
            const res = await AuthService.logout(requestBody);
            if (res.status === HttpStatusCode.Ok) {
                setBrand({ id: "", name: "" });
                setToken({ accessToken: "", refreshToken: "", accessTokenExpiresAt: "" });
                localStorage.removeItem("brand");
                localStorage.removeItem("token");
                navigate("/login");
            }
        } catch (error) {
            console.log("error: ", error);
        }
    }, [navigate]);

    const refreshAccessToken = useCallback(async () => {
        try {
            const res = await AuthService.refreshToken(token.refreshToken);
            if (res.status === HttpStatusCode.Ok) {
                const { accessToken, accessTokenExpiresAt, refreshToken } = res.data;
                const newToken = { accessToken, accessTokenExpiresAt, refreshToken };
                setToken(newToken);
                localStorage.setItem('token', JSON.stringify(newToken));
            } else {
                logOut(token); // If refresh fails, log the user out
            }
        } catch (error) {
            console.error("Failed to refresh token:", error);
            logOut(token);
        }
    }, [token, logOut]);

    useEffect(() => {
        // Set interval to check token expiration
        const interval = setInterval(() => {
            if (token.accessToken && Date.now() / 1000 > token.accessTokenExpiresAt - 60) {
                refreshAccessToken();
            }
        }, 10000); // Check every 10 seconds

        return () => clearInterval(interval); // Clean up the interval on unmount
    }, [token.accessToken, token.accessTokenExpiresAt, refreshAccessToken]);

    const loginAction = async (data) => {
        const requestBody = JSON.stringify(data);
        try {
            const res = await AuthService.login(requestBody);
            if (res.status === HttpStatusCode.Ok) {
                const { id, username: name, accessToken, refreshToken, accessTokenExpiresAt } = res.data;
                const newBrand = { id, name };
                const newToken = { accessToken, refreshToken, accessTokenExpiresAt };
                setBrand(newBrand);
                setToken(newToken);
                localStorage.setItem('brand', JSON.stringify(newBrand));
                localStorage.setItem('token', JSON.stringify(newToken));
                navigate('/');
            }
        } catch (error) {
            console.log("error: ", error);
        }
    };

    return (
        <AuthContext.Provider value={{ token, brand, loginAction, logOut }}>
            {children}
        </AuthContext.Provider>
    );
};

export default AuthProvider;

export const useAuth = () => {
    return useContext(AuthContext);
};
