import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ISector, defaultValue } from 'app/shared/model/sector.model';

export const ACTION_TYPES = {
  FETCH_SECTOR_LIST: 'sector/FETCH_SECTOR_LIST',
  FETCH_SECTOR: 'sector/FETCH_SECTOR',
  CREATE_SECTOR: 'sector/CREATE_SECTOR',
  UPDATE_SECTOR: 'sector/UPDATE_SECTOR',
  DELETE_SECTOR: 'sector/DELETE_SECTOR',
  RESET: 'sector/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISector>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type SectorState = Readonly<typeof initialState>;

// Reducer

export default (state: SectorState = initialState, action): SectorState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SECTOR_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SECTOR):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SECTOR):
    case REQUEST(ACTION_TYPES.UPDATE_SECTOR):
    case REQUEST(ACTION_TYPES.DELETE_SECTOR):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_SECTOR_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SECTOR):
    case FAILURE(ACTION_TYPES.CREATE_SECTOR):
    case FAILURE(ACTION_TYPES.UPDATE_SECTOR):
    case FAILURE(ACTION_TYPES.DELETE_SECTOR):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_SECTOR_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_SECTOR):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SECTOR):
    case SUCCESS(ACTION_TYPES.UPDATE_SECTOR):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SECTOR):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/sectors';

// Actions

export const getEntities: ICrudGetAllAction<ISector> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_SECTOR_LIST,
    payload: axios.get<ISector>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<ISector> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SECTOR,
    payload: axios.get<ISector>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ISector> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SECTOR,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ISector> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SECTOR,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISector> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SECTOR,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
