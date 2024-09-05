import React, { useEffect, useState } from 'react';
import { getStatistics } from '../services/StatisticsService'; // Import service function
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper } from '@mui/material';

const Statistics = () => {
    const [profitData, setProfitData] = useState([]);

    useEffect(() => {
        const brand = JSON.parse(localStorage.getItem('brand'));
        const brandID = brand?.id;

        if (brandID) {
            getStatistics(brandID)  // Use the service function
                .then(data => setProfitData(data))
                .catch(error => console.error('Error fetching profit data:', error));
        }
    }, []);

    return (
        <div>
            <h1>Brand Profit Statistics</h1>

            <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>Event Name</TableCell>
                            <TableCell>Profit</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {profitData.map((row, index) => (
                            <TableRow key={index}>
                                <TableCell>{row.eventName}</TableCell>
                                <TableCell>{row.value * row.count}</TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>

            <ResponsiveContainer width="100%" height={400}>
                <LineChart data={profitData}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="eventName" />
                    <YAxis />
                    <Tooltip />
                    <Legend />
                    <Line type="monotone" dataKey="profit" stroke="#8884d8" />
                </LineChart>
            </ResponsiveContainer>
        </div>
    );
};

export default Statistics;
