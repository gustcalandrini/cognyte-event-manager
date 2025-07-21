import { Menu } from 'antd';
import { CalendarOutlined, HomeOutlined } from '@ant-design/icons';
import { useLocation, useNavigate } from 'react-router-dom';

const SiderMenu = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const menuItems = [
    {
      key: '/home',
      icon: <HomeOutlined />,
      label: 'Home',
      onClick: () => navigate('/home'),
    },
    {
      key: '/events',
      icon: <CalendarOutlined />,
      label: 'Events',
      onClick: () => navigate('/events'),
    },
  ];

  return (
    <Menu
      theme="dark"
      mode="inline"
      selectedKeys={[location.pathname]}
      onClick={(item) => navigate(item.key)}
      defaultSelectedKeys={['/home']}
      items={menuItems}
    />
  );
};

export default SiderMenu;
