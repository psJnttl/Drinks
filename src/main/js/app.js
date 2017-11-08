import React from 'react';
import PropTypes from 'prop-types';
import {NavLink, BrowserRouter as Router, Route,  Link, Switch} from 'react-router-dom';
import Employees from './Employees';
import Navi from './components/Navi';
import Logout from './Logout';
import Ingredients from './components/Ingredients';
import Glasses from './components/Glasses';
import Categories from './components/Categories';

const Home = () => (
  <div>
    Home page of the application.
  </div>
);

const About = () => (
  <div>
    This is the Drinks archive app.
  </div>
);

class App extends React.Component {
  constructor(props) {
    super(props);
  }
  render() {
    const authValue = this.props.authenticated;
    return (
      <Router>
        <div >
          <Navi
            authenticated={this.props.authenticated}
            changeAuthState={this.props.changeAuthState}
          />
          <Switch>
            <Route exact path="/" component={Home} />
            <Route path="/ingredients" component={Ingredients} />
            <Route path="/glasses" component={Glasses} />
            <Route path="/categories" component={Categories} />
            <Route path="/about" component={About} />
            <Route path="/logout" render={(props) => (
              <Logout authenticated={authValue} changeAuthState={this.props.changeAuthState} {...props}  />
            )}/>
            <Route render={ () => <div><h1>404 - Not Found!</h1></div>} />
          </Switch>
        </div>
      </Router>
    );
  }
}
App.PropTypes = {
  authenticated: PropTypes.bool.isRequired,
  changeAuthState: PropTypes.func.isRequired,
}
App.defaultProps = {}

export default App;
