//a modal component for signup

import React from "react";
import { Link } from "react-router-dom";

function SignupPage() {
   
  return (
    <>
      <div className="fn-signup-signin-wraper">
        <div className="fn-container-login">
          <div className="text-center">
            <h3>New User</h3>
          </div>
          <div className="fn-input-area">
            <input type="text" placeholder="Email Id" />
            <input type="text" placeholder="Username" />
            <input type="password" placeholder="Password" />
            <input type="password" placeholder="Confirm Password" />
          </div>
          
          <div className="text-center">
            <button type="submit" value="sign-up" className="btn fn-btn-pink">Sign Up</button>
            <div className="mt-3">
              Already have an account ?
              <p className="fn-signup-link-green" >
                <Link to="/login">Log In</Link>
              </p>
            </div>
            <p className="fn-text-petite text-muted"><input type="checkbox" checked disabled style={{display:"inline-block",marginRight:"1em"}}/>By logging in or signing up, you automatically agree to our <Link to="/terms-and-conditions" className="fn-nav-link">terms and conditions</Link>.</p>
          </div>
        </div>
      </div>
    </>
  )
}
export default SignupPage;