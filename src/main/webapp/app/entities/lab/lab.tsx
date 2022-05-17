import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ILab } from 'app/shared/model/lab.model';
import { getEntities } from './lab.reducer';

export const Lab = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const labList = useAppSelector(state => state.lab.entities);
  const loading = useAppSelector(state => state.lab.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="lab-heading" data-cy="LabHeading">
        Labs
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to="/lab/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Lab
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {labList && labList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Job Title</th>
                <th>Min Salary</th>
                <th>Max Salary</th>
                <th>Student</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {labList.map((lab, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/lab/${lab.id}`} color="link" size="sm">
                      {lab.id}
                    </Button>
                  </td>
                  <td>{lab.jobTitle}</td>
                  <td>{lab.minSalary}</td>
                  <td>{lab.maxSalary}</td>
                  <td>
                    {lab.students
                      ? lab.students.map((val, j) => (
                          <span key={j}>
                            <Link to={`/student/${val.id}`}>{val.id}</Link>
                            {j === lab.students.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/lab/${lab.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/lab/${lab.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/lab/${lab.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Labs found</div>
        )}
      </div>
    </div>
  );
};

export default Lab;
