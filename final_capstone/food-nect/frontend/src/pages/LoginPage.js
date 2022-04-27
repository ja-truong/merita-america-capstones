//a modal page for sign in

import { Link } from "react-router-dom";

function LoginPage(){

  const spanclicked=()=>{
    console.log("000")
  }
  
  return (
    <>
      <div className="fn-signup-signin-wraper">
        <div className="fn-container-login">
          <div className="text-center">
            <h3>Welcome</h3>
          </div>
          <div className="fn-input-area">
            <input type="text" placeholder="Username" />
            <input type="password" placeholder="Password" />
          </div>
          <div className="text-center">
            <button type="submit" value="log-in" className="btn fn-btn-pink">Log In</button>
            <div className="mt-3">
              Don't have a account ?
              <p className="fn-signup-link-green" >
                <Link to="/signup">Sign Up</Link>
              </p>
            </div>
            <p className="fn-text-petite text-muted"><input type="checkbox" checked disabled style={{display:"inline-block",marginRight:"1em"}}/>By logging in or signing up, you automatically agree to our <Link to="/terms-and-conditions" className="fn-nav-link">terms and conditions</Link>.</p>
          </div>
        </div>
      </div>
    </>
  )
}

export default LoginPage;