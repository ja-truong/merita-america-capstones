//header component with authenticated navigation links (nav links to access authenticated resources: settings, meetings, friends list, ...)
//inject to HeaderWraperComponent if not authenticated

import React from 'react';
import {FaEnvelopeOpenText, FaBell, FaCalendarAlt, FaSlidersH} from "react-icons/fa"

export default function HeaderAuthenticatedComponent() {
  return (
    <>
        <ul className="fn-tools-bar">
            <li className="fn-selected-tool">
                <FaEnvelopeOpenText />
            </li>
            <li>
                <FaBell />
            </li>
            <li>
                <FaCalendarAlt />
            </li>
            <li style={{transform: "rotate(90deg)"}}>
                <FaSlidersH />
            </li>
        </ul>
    </>
  )
}
