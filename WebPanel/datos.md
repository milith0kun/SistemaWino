ssh -i "Wino.pem" ubuntu@ec2-18-188-209-94.us-east-2.compute.amazonaws.com
cd ~/SistemaWino
git pull
cd WebPanel
npm run build