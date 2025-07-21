import { Navigate, Route, Routes } from "react-router-dom";
import Home from "../../pages/home/home";
import PageNotFound from "../../pages/error/page-not-found";
import MainLayout from "../layout/layout";
import Events from "../../pages/event/event";
import EventUpdate from "../../pages/event/event-update";

const AppRoutes = () => {
  return (
    <Routes>
      <Route path="/home" element={
        <MainLayout>
          <Home />
        </MainLayout>
      } />
      <Route path="/events" element={
        <MainLayout>
          <Events />
        </MainLayout>
      } />
      <Route path="/events/:id/edit" element={<EventUpdate />} />
      <Route path="*" element={<PageNotFound />} />
      <Route path="/" element={<Navigate to="/home" replace />} />
    </Routes>
  );
}

export default AppRoutes;