import PropTypes from 'prop-types';
import { useParams, useNavigate } from 'react-router-dom';

const ItemDetails = ({ items }) => {
    const { id } = useParams();
    const navigate = useNavigate();
    const item = items.find(i => i.id == id);
    
    if (!item) {
        console.log(item);
        return navigate('/');
    }

    return (
        <>
            <div className="input-group mb-2">
                <div className="input-group-prepend">
                    <span className="input-group-text">Title</span>
                </div>
                <input type="text" className="form-control" value={item.title} readOnly></input>
            </div>
            <div className="input-group mb-2">
                <div className="input-group-prepend">
                    <span className="input-group-text">Description</span>
                </div>
                <input type="text" className="form-control" value={item.description} readOnly></input>
            </div>
            <h1>Put your ads here</h1>
        </>
    );
};

ItemDetails.propTypes = {
    item: PropTypes.object
};

export default ItemDetails;