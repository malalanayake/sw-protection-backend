cd /Volumes/Dev/Jmeter/apache-jmeter-2.11/bin

rm -rf Result.jtl

sh jmeter.sh -n -t "/Volumes/Dev/Git/sw-protection-backend/jmeter-tests/Admin-REST-API-test.jmx" -l "/Volumes/Dev/Result.jtl"

sh jmeter.sh -n -t "/Volumes/Dev/Git/sw-protection-backend/jmeter-tests/AdminScope-REST-API-test.jmx" -l "/Volumes/Dev/Result.jtl"

sh jmeter.sh -n -t "/Volumes/Dev/Git/sw-protection-backend/jmeter-tests/Company-REST-Api-test.jmx" -l "/Volumes/Dev/Result.jtl"

sh jmeter.sh -n -t "/Volumes/Dev/Git/sw-protection-backend/jmeter-tests/CompanyClient-REST-Api-test.jmx" -l "/Volumes/Dev/Result.jtl"

sh jmeter.sh -n -t "/Volumes/Dev/Git/sw-protection-backend/jmeter-tests/CompanySW-REST-Api-test.jmx" -l "/Volumes/Dev/Result.jtl"

sh jmeter.sh -n -t "/Volumes/Dev/Git/sw-protection-backend/jmeter-tests/CompanySWCopy-REST-Api-test.jmx" -l "/Volumes/Dev/Result.jtl"

sh jmeter.sh -n -t "/Volumes/Dev/Git/sw-protection-backend/jmeter-tests/CompanyUser-REST-Api-test.jmx" -l "/Volumes/Dev/Result.jtl"

sh jmeter.sh -n -t "/Volumes/Dev/Git/sw-protection-backend/jmeter-tests/CompanyUserScope-REST-API-test.jmx" -l "/Volumes/Dev/Result.jtl"