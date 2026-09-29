import {BrowserRouter as Router,Route,Routes, Navigate} from "react-router-dom";
import Login from "./pages/Login.jsx";
import Register from"./pages/Register.jsx"
import Feed from "./pages/Feed.jsx";
import Chat from './pages/Chat';
import Network from './pages/Network';

function App()
{
  const isAuthenticated=!! localStorage.getItem('token');


  return (
      <Router>
        <Routes>
          {/*Public Routes*/}
          <Route path="/login" element={<Login/>}/>
          <Route path="/register" element={<Register/>}/>

          {/*Protected Routes (if not logged in redirect it to login page*/}
          <Route
            path="/feed"
            element={isAuthenticated?<Feed/>:<Navigate to="/login"/>}
            />
          {/*Default Route*/}
          <Route path="*" element={<Navigate to="/login" />} />
            <Route path="/chat" element={isAuthenticated ? <Chat /> : <Navigate to="/login" />} />
            <Route path="/network" element={isAuthenticated ? <Network /> : <Navigate to="/login" />} />

        </Routes>
      </Router>
  );
}

export default App;