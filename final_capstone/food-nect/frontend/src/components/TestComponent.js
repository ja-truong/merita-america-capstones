import React, { useState } from "react";
import SendLink from "./SendLinkComponent";
import {Button } from 'reactstrap';

export default function Test() {
  const [buttonPopup, setButtonPopup] = useState(false);
  return (
    <>
        <h1>Lorem ipsum</h1>
        <p>
        Auctor dolor nibh enim et inceptos massa senectus duis. Velit nibh adipiscing natoque, class suscipit. Vitae viverra facilisis amet accumsan eleifend venenatis nulla enim imperdiet blandit odio tempor. Pellentesque netus cubilia consectetur molestie euismod imperdiet cras id nisi varius vestibulum. Maecenas quis vivamus iaculis cras imperdiet tristique odio accumsan? Mus dui magnis duis etiam. Lacinia fermentum luctus fringilla consectetur. Cras iaculis morbi varius nunc? Sociosqu nam urna tellus. Mauris senectus quam curabitur pretium sollicitudin. Varius ad suscipit iaculis sociosqu tincidunt rutrum platea ante molestie. Habitasse laoreet, lacinia quisque nisi.
        </p>
        <p>
            Sociosqu vehicula proin varius elit cursus duis sapien tortor, porttitor quis. Tristique elementum dictum etiam hac placerat erat fames tellus? Amet habitasse non lectus aptent etiam lorem habitant nullam dictumst. Per curae; dapibus egestas sollicitudin fusce gravida feugiat sapien in aenean sem praesent. Vestibulum fames ipsum habitasse dictumst ultrices ligula. Vulputate arcu natoque volutpat phasellus habitant placerat sociis morbi lacinia aptent. Curae; convallis commodo, commodo laoreet ullamcorper nulla. Leo ipsum gravida turpis lacus erat felis porta natoque. Commodo pellentesque etiam iaculis aptent nisi convallis, luctus.
        </p>
        <p>
            Euismod ac blandit amet eros. Egestas imperdiet velit dolor. Integer ligula luctus non non. Molestie suspendisse, mauris purus faucibus blandit curae; convallis arcu fames? Enim sapien et aenean placerat suspendisse ullamcorper fringilla cras torquent class auctor! Lacinia facilisi senectus iaculis! Purus porta nascetur suscipit eu vel placerat odio rutrum metus inceptos. Rutrum class nostra fringilla. Placerat nunc class.
        </p>
        <p>
            Nulla libero, habitant nisl nisi. Tortor neque mattis mollis! Nam fringilla viverra sociis nec. Erat laoreet mauris gravida diam molestie justo dictum porta. Consequat orci dis dui. Rhoncus porttitor consequat consectetur dolor dapibus sodales nascetur auctor aenean ridiculus integer. Porttitor ultricies morbi neque dolor a dignissim libero fames maecenas, proin vel. Commodo rhoncus nam potenti himenaeos congue arcu risus nisl nec convallis ut posuere. Arcu magna.
        </p>
        <p>
            Etiam turpis ultrices rutrum libero curae; natoque, sed malesuada accumsan mollis. Quis per in duis integer sem massa justo commodo. Porttitor potenti leo, venenatis pharetra consectetur natoque consectetur bibendum convallis in rutrum. Nulla integer tempus rhoncus sodales maecenas eros aliquam laoreet venenatis bibendum libero hac! Rhoncus duis purus nunc quisque laoreet condimentum metus accumsan pellentesque cum. Tortor pharetra egestas donec eu.
        </p>
        <Button color="primary" className='m-2' onClick={() => setButtonPopup(true)}>
            <i className='fa fa-pencil fa-lg'></i> Send Link
        </Button>
        <SendLink trigger={buttonPopup} setTrigger={setButtonPopup} decideBy="tommorow" baseUrl='https://jasper.free.beeceptor.com' restaurants=''/>
    </>
  )
}