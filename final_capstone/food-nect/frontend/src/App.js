//main component

import React from "react";
import { BrowserRouter } from "react-router-dom";
import HeaderWraperComponent from "./components/HeaderWraperComponent";
import WraperContainer from "./pages/WraperContainer";
import FooterComponent from "./components/FooterComponent";

function App(){
  return (
    <>
      <BrowserRouter>
          <HeaderWraperComponent />
          <WraperContainer />

          <FooterComponent />
      </BrowserRouter>
    </>
  );
}

export default App;
