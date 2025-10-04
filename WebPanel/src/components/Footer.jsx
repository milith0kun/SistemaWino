import { Box, Typography, Link, Container, Divider } from '@mui/material';
import { Email as EmailIcon, Business as BusinessIcon, Code as CodeIcon, Phone as PhoneIcon, LinkedIn as LinkedInIcon } from '@mui/icons-material';

const Footer = () => {
  return (
    <Box
      component="footer"
      sx={{
        py: 3,
        px: 2,
        mt: 'auto',
        backgroundColor: '#1a1d2e',
        color: '#fff',
      }}
    >
      <Container maxWidth="lg">
        <Box
          sx={{
            display: 'flex',
            flexDirection: { xs: 'column', md: 'row' },
            justifyContent: 'space-between',
            alignItems: { xs: 'center', md: 'flex-start' },
            gap: 3,
          }}
        >
          {/* Sistema Info */}
          <Box sx={{ textAlign: { xs: 'center', md: 'left' } }}>
            <Typography variant="h6" gutterBottom sx={{ fontWeight: 600, display: 'flex', alignItems: 'center', gap: 1 }}>
              <CodeIcon /> Sistema HACCP Wino
            </Typography>
            <Typography variant="body2" color="rgba(255,255,255,0.7)">
              Sistema de Control de Calidad y Asistencias
            </Typography>
            <Typography variant="body2" color="rgba(255,255,255,0.7)" sx={{ mt: 1 }}>
              © {new Date().getFullYear()} - Todos los derechos reservados
            </Typography>
          </Box>

          <Divider orientation="vertical" flexItem sx={{ borderColor: 'rgba(255,255,255,0.1)', display: { xs: 'none', md: 'block' } }} />

          {/* Developer Info */}
          <Box sx={{ textAlign: { xs: 'center', md: 'right' } }}>
            <Typography variant="body2" color="rgba(255,255,255,0.5)" gutterBottom>
              Desarrollado por
            </Typography>
            <Typography variant="h6" sx={{ fontWeight: 600, mb: 1 }}>
              Edmil Jampier Saire Bustamante
            </Typography>
            
            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 0.5 }}>
              <Link
                href="mailto:174449@unsaac.edu.pe"
                sx={{
                  color: 'rgba(255,255,255,0.8)',
                  textDecoration: 'none',
                  display: 'flex',
                  alignItems: 'center',
                  gap: 1,
                  justifyContent: { xs: 'center', md: 'flex-end' },
                  '&:hover': {
                    color: '#4ADE80',
                  },
                }}
              >
                <EmailIcon fontSize="small" />
                <Typography variant="body2">174449@unsaac.edu.pe</Typography>
              </Link>
              
              <Link
                href="https://ecosdelseo.com"
                target="_blank"
                rel="noopener noreferrer"
                sx={{
                  color: 'rgba(255,255,255,0.8)',
                  textDecoration: 'none',
                  display: 'flex',
                  alignItems: 'center',
                  gap: 1,
                  justifyContent: { xs: 'center', md: 'flex-end' },
                  '&:hover': {
                    color: '#4ADE80',
                  },
                }}
              >
                <BusinessIcon fontSize="small" />
                <Typography variant="body2">ecosdelseo.com</Typography>
              </Link>
              
              <Link
                href="https://www.linkedin.com/in/edmilsaire/"
                target="_blank"
                rel="noopener noreferrer"
                sx={{
                  color: 'rgba(255,255,255,0.8)',
                  textDecoration: 'none',
                  display: 'flex',
                  alignItems: 'center',
                  gap: 1,
                  justifyContent: { xs: 'center', md: 'flex-end' },
                  '&:hover': {
                    color: '#4ADE80',
                  },
                }}
              >
                <LinkedInIcon fontSize="small" />
                <Typography variant="body2">linkedin.com/in/edmilsaire</Typography>
              </Link>
              
              <Box
                sx={{
                  color: 'rgba(255,255,255,0.8)',
                  display: 'flex',
                  alignItems: 'center',
                  gap: 1,
                  justifyContent: { xs: 'center', md: 'flex-end' },
                }}
              >
                <PhoneIcon fontSize="small" />
                <Typography variant="body2">+51 901246936</Typography>
              </Box>
            </Box>
          </Box>
        </Box>
      </Container>
    </Box>
  );
};

export default Footer;
