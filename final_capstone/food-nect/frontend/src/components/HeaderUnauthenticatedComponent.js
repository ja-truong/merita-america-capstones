//header component with unauthenticated navigation links (singup, login)
//inject to HeaderWraperComponent if not authenticated

import React from 'react'

function HeaderUnauthenticatedComponent() {
  return (
    <>
      <div className="btn fn-btn-pink">Sign In</div>
      <div className="d-inline-block ms-3 fn-text-light-pink">
        <p className="m-0 p-0">
          New user? <span className="fn-signup-link">Signup</span>
        </p>
      </div>
    </>
  )
}

export default HeaderUnauthenticatedComponent;