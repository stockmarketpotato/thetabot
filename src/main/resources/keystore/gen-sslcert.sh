# interactive
# openssl req -x509 -newkey rsa:4096 -keyout key.pem -out cert.pem -sha256 -days 365

StateName="StateName"
CityName="CityName"
CompanyName="CompanyName"
CompanySectionName="CompanySectionName"
CommonNameOrHostname="localhost"

# non-interactive and 10 years expiration
MSYS_NO_PATHCONV=1 openssl req -x509 -newkey rsa:4096 -keyout key.pem -out cert.pem -sha256 -days 3650 -nodes -subj "/C=XX/ST=$StateName/L=$CityName/O=$CompanyName/OU=$CompanySectionName/CN=$CommonNameOrHostname"
