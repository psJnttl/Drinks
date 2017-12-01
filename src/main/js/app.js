import React from 'react';
import PropTypes from 'prop-types';
import {NavLink, BrowserRouter as Router, Route,  Link, Switch} from 'react-router-dom';
import Employees from './Employees';
import Navi from './components/Navi';
import Logout from './Logout';
import Ingredients from './components/Ingredients';
import Glasses from './components/Glasses';
import Categories from './components/Categories';
import EventLog from './components/EventLog';
import Drinks from './components/Drinks';
import Account from './components/Account';
import Admin from './components/Admin';
import _ from 'lodash';
import NetworkApi from './components/NetworkApi';

const About = () => (
  <div>
    This is the Drinks archive app.
  </div>
);

class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {categories: [], glasses: [], ingredients: []};
    this.setList = this.setList.bind(this);
    this.fetchItems = this.fetchItems.bind(this);
  }

  setList(no, data) {
    const theList = data.map( item =>
       _.assign({}, {id: item.id, name: item.name}) );
    if (1 === no) {
      this.setState({categories: theList});
    }
    else if ( 2 === no) {
      this.setState({glasses: theList});
    }
    else if (3 === no) {
      this.setState({ingredients: theList});
    }
  }

  fetchItems(entity, listNo) {
    const self = this;
    NetworkApi.get('api/' + entity)
         .then(function (response) {
            self.setList(listNo, response);
         })
         .catch(function (error) {

         });
  }

  componentDidMount() {
    this.fetchItems("categories", 1);
    this.fetchItems("glasses", 2);
    this.fetchItems("ingredients", 3);
  }

  render() {
    const authState = this.props.authState;
    const admin = this.props.authState.admin;
    return (
      <Router>
        <div >
          <Navi
            authState={this.props.authState}
            changeAuthState={this.props.changeAuthState}
          />
          <Switch>
            <Route exact path="/" render={ (props) => (
              <Drinks
                categories={this.state.categories}
                glasses={this.state.glasses}
                ingredients={this.state.ingredients} />
            )} />
            <Route path="/ingredients" render={(props) => (
              <Ingredients updateRoot={this.setList} {...props} />
            )} />
            <Route path="/glasses" render={(props) => (
              <Glasses updateRoot={this.setList} {...props} />
            )} />
            <Route path="/categories" render={(props) => (
              <Categories updateRoot={this.setList} {...props} />
            )} />
            <Route path="/about" component={About} />
            <Route path="/logout" render={(props) => (
              <Logout authenticated={authState.authenticated} changeAuthState={this.props.changeAuthState} {...props}  />
            )}/>
            <Route path="/account" render={(props) => (
              <Account authState={authState} {...props} />
            )} />
            <Route path="/admin" render={(props) => (
              <Admin authState={authState} {...props}  />
            )}/>
            <Route render={ () => <div><h1>404 - Not Found!</h1></div>} />
          </Switch>
        </div>
      </Router>
    );
  }
}
App.PropTypes = {
  authState: PropTypes.object.isRequired,
  changeAuthState: PropTypes.func.isRequired,
}
App.defaultProps = {}

export default App;
