import React from 'react';
import PropTypes from 'prop-types';
import {NavLink, BrowserRouter as Router, Route,  Link, Switch} from 'react-router-dom';
import Employees from './Employees';
import Navi from './components/Navi';

const Home = () => (
  <div>
    Home page of the application.
  </div>
);

const About = () => (
  <div>
    This is a base project for Routing.
  </div>
);

class App extends React.Component {
  constructor(props) {
    super(props);
  }
  render() {
    return (
      <Router>
        <div >
          <Navi />
          <Switch>
            <Route exact path="/" component={Home} />
            <Route path="/employees" component={Employees} />
            <Route path="/about" component={About} />
            <Route render={ () => <div><h1>404 - Not Found!</h1></div>} />
          </Switch>
        </div>
      </Router>
    );
  }
}
App.PropTypes = {}
App.defaultProps = {}

export default App;
