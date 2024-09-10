import React, { useEffect, useState } from 'react';
import { getStatistics, getParticipants } from '../services/StatisticsService'; // Import service function
import { Bar } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend } from 'chart.js';
import {Grid} from "@mui/material";

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);

const BudgetStatistics = () => {
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
                .catch(error => console.error('Error fetching participant data:', error));
        }
    }, []);

    const budgetChartData = {
        labels: budgetData.map(item => item.eventName), // X-axis labels
        datasets: [
            {
                label: 'Budget',
                data: budgetData.map(item => item.budget), // Y-axis data
                backgroundColor: 'rgba(75, 192, 192, 0.5)',
                borderColor: 'rgba(75, 192, 192, 1)',
                borderWidth: 1,
            },
        ],
    };

    const participantChartData = {
        labels: participantData.map(item => item.eventName), // X-axis labels from event names
        datasets: [
            {
                label: 'Participants',
                data: participantData.map(item => item.participantCount), // Y-axis data for participants
                backgroundColor: 'rgba(153, 102, 255, 0.5)',
                borderColor: 'rgba(153, 102, 255, 1)',
                borderWidth: 1,
            },
        ],
    };

    const options = {
        responsive: true,
        plugins: {
            legend: {
                position: 'top',
            },
        },
    };

    return (
        <div className='mt-5'>
            <Grid container spacing={2}>
                <Grid item xs={6}>
                    <h1>Brand Budget Statistics</h1>
                    <Bar data={budgetChartData} options={options}/>
                </Grid>
                <Grid item xs={6}>
                    <h1>Brand Participant Statistics</h1>
                    <Bar data={participantChartData} options={options}/>
                </Grid>
            </Grid>
        </div>
    );
};

export default BudgetStatistics;
