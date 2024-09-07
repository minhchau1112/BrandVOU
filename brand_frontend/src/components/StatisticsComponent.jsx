import React, { useEffect, useState } from 'react';
import { getStatistics, getParticipants } from '../services/StatisticsService'; // Import service function
import { LineChart, Line, BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import { Grid } from '@mui/material';

const Statistics = () => {
    const [budgetData, setBudgetData] = useState([]);
    const [participantData, setParticipantData] = useState([]);

    useEffect(() => {
        const brand = JSON.parse(localStorage.getItem('brand'));
        const brandID = brand?.id;

        if (brandID) {
            getStatistics(brandID)  // Use the service function
                .then(data => setBudgetData(data))
                .catch(error => console.error('Error fetching profit data:', error));

            getParticipants(brandID)
                .then(data => setParticipantData(data))
                .catch(error => console.error('Error fetching profit data:', error));
        }
    }, []);

    return (
        <Grid container spacing={2}>
            <Grid item xs={6}>
                <h1>Brand Participant Statistics</h1>
                <ResponsiveContainer width="100%" height={400}>
                    <BarChart data={participantData} margin={{ top: 10, right: 30, left: 50, bottom: 5 }}>
                        <CartesianGrid strokeDasharray="3 3"/>
                        <XAxis dataKey="eventName"/>
                        <YAxis/>
                        <Tooltip/>
                        <Legend/>
                        <Bar type="monotone" dataKey="participantCount" stroke="#8884d8" fill="#b1e7f0"/>
                    </BarChart>
                </ResponsiveContainer>
            </Grid>
            <Grid item xs={6}>
                <h1>Brand Budget Statistics</h1>
                <ResponsiveContainer width="100%" height={400}>
                    <BarChart data={budgetData} margin={{ top: 10, right: 30, left: 50, bottom: 5 }}>
                        <CartesianGrid strokeDasharray="3 3"/>
                        <XAxis dataKey="eventName"/>
                        <YAxis/>
                        <Tooltip/>
                        <Legend/>
                        <Bar type="monotone" dataKey="budget" stroke="#8884d8" fill="#b1e7f0"/>
                    </BarChart>
                </ResponsiveContainer>
            </Grid>
        </Grid>
    );
};

export default Statistics;
