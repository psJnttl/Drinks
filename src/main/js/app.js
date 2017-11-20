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
import _ from 'lodash';
import axios from 'axios';

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
    const config = {headers: {'X-Requested-With': 'XMLHttpRequest'}};
    const self = this;
    axios.get('api/' + entity, config)
         .then(function (response) {
            self.setList(listNo, response.data);
         })
         .catch(function (response) {

         });
  }

  componentDidMount() {
    this.fetchItems("categories", 1);
    this.fetchItems("glasses", 2);
    this.fetchItems("ingredients", 3);
  }

  render() {
    const authValue = this.props.authState.authenticated;
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
              <Logout authenticated={authValue} changeAuthState={this.props.changeAuthState} {...props}  />
            )}/>
            <Route path="/account" render={(props) => (
              <Account authState={this.props.authState} {...props} />
            )} />
            <Route path="/eventlog" render={(props) => (
              <EventLog authenticated={authValue} {...props}  />
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
