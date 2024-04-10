import React from 'react';
import Events from '../components/Events';

function Home() {
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    return (
        <div>
            <Events/>
        </div>
    )
}

export default Home;