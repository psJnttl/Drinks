import React from 'react';
import PropTypes from 'prop-types';
import axios from 'axios';


class Employees extends React.Component {
  constructor(props) {
    super(props);
    this.state = {employees: [], }
        this.employeesLoaded = this.employeesLoaded.bind(this);
  }
  employeesLoaded(data) {
    console.log(data);
    this.setState({employees: data});
  }
  componentDidMount() {
    const self = this;
    axios.get('api/employees')
         .then(function (response) {
             self.employeesLoaded(response.data); //console.log(response);
         })
        .catch(function (response) {
            console.log(response);
        });
  }

  render() {
    const titles = ["First Name", "Last Name", "Description", "Manager"];
    const header = titles.map((item, i) => <th key={i}>{item}</th>);
    const data = this.state.employees.map((row, i) =>
      <tr key={i}>
        <td>{row.firstName}</td>
        <td>{row.lastName}</td>
        <td>{row.description}</td>
        <td>{row.managerName}</td>
      </tr>
  );
    return (
      <div>
        <h4>Employee table</h4>
        <table>
          <thead>
            <tr>{header}</tr>
          </thead>
          <tbody>
            {data}
          </tbody>
        </table>
      </div>

    );
  }
}
Employees.PropTypes = {

}
Employees.defaultProps = {}
export default Employees;
