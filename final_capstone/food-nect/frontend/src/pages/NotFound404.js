import React from 'react'

const NotFound404 = () => {
  const notFoundSvg = process.env.PUBLIC_URL + "/images/not-found-404.svg"
  return (
    <>
      <div className="fn-not-found-wraper">
        <div>
          <img src={notFoundSvg} />
          </div>
        <div>
          <h1 className="fn-not-found-text">Page n<span className="fn-not-found-o">o</span> t found</h1>
        </div>
      </div>
    </>
  )
}
export default NotFound404;
