//header component with authenticated navigation links

import React, {useState} from 'react'
import HeaderAuthenticatedComponent from "./HeaderAuthenticatedComponent";
import HeaderUnauthenticatedComponent from "./HeaderUnauthenticatedComponent";

function HeaderWraperComponent() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  return (
    <>
      <div className="fluid-container p-3 fn-header-dark">
        <div id="header-wrapper-inner" className="container">
          <div className="d-flex">
            <div className="fn-logo text-white">FN</div>
            <div className="d-flex justify-content-end align-items-center flex-grow-1">
              {isAuthenticated && <HeaderUnauthenticatedComponent />}

              {!isAuthenticated && <HeaderAuthenticatedComponent />}
            </div>
          </div>
        </div>
      </div>
    </>
  )
}

export default HeaderWraperComponent;