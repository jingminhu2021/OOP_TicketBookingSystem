import React from 'react';
import { useNavigate } from "react-router-dom";
import { useEffect } from 'react';

function Logout(){
    localStorage.clear()
    const navigate = useNavigate();
    useEffect(() => {
        navigate('/')
    }, [])

    return (
        <>
        </>
    )
}
export default Logout