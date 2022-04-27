import React, {Component} from 'react';

function Test1 () {
        console.log("Test1 compo")

        const procDate = () => {
            const tme = "11:30"
            const e = new Date("2000-01-01 " + tme);
            const d = new Date();
            const j = (d.getHours() * 3600) + (d.getMinutes() * 60);
            console.log("test1: " + j);

            console.log("e: " + e.getHours());
        }

        return (
            <>
                {procDate()}
                <p>test</p>
            </>
        );



}

export default Test1;