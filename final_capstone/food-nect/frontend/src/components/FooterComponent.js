//footer component common to every page including authorized or non-authorized page

import React from 'react'

function FooterComponent() {
  const curvedSvg = process.env.PUBLIC_URL + "/images/curved.svg";

  const year = (new Date()).getFullYear()
  return (
    <>
      <div  className="fn-footer">
        <div className="fn-curved-svg-wraper">
        <img src={curvedSvg}/>
        </div>
        <div className="text-center">
          <div className="container">
            <p className="p-2 m-0">&copy; copy right ({year})</p>
            <p className="fn-footer-creators">made with ♡ by Surendra, Grace, Jasper &amp; Vick</p>
          </div>
        </div>
      </div>
    </>
  )
}
export default FooterComponent;