import React from 'react';

const ThreeDText = ({ text, color = 'text-gray-800', size = 'text-4xl' }) => {
    return (
        <div className="relative inline-block">
            <span className={`absolute top-1 left-1 ${size} font-bold ${color} opacity-70`}>{text}</span>
            {/* <span className={`absolute top-2 left-2 ${size} font-bold ${color} opacity-50`}>{text}</span> */}
            <span className={`absolute top-2 left-2 ${size} font-bold ${color} opacity-30`}>{text}</span>
            <span className={`relative ${size} font-bold ${color}`}>{text}</span>
        </div>
    );
};

export default ThreeDText;