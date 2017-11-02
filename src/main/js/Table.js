import React from 'react';
import PropTypes from 'prop-types';

class Table extends React.Component {

  formRow(rawRow, keys) {
    console.log(rawRow);
    console.log(keys);
    return rawRow.map( (td, index) => <td>{td[keys[index]]}</td> );
  }
  render() {
    const header = this.props.header.map( (th, index) =>
      <th key={index}>{th.value}</th>
    );
    const keys = this.props.header.map( (th) => th.key );
    const data = this.props.data.map( (row, index) =>
      <tr>{this.formRow(row, keys)}</tr> );

    return (
      <table>
        <thead>
          <tr>{header}</tr>
        </thead>
        <tbody>
          {data}
        </tbody>
      </table>

    );
  }
}

Table.PropTypes = {
  header: PropTypes.array.isRequired,
  data: PropTypes.array.isRequired,
}
export default Table;
