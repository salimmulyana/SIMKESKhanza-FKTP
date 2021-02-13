#------------------Section 1 = Settings------------------

# Name of server | type your server name for identification yout backup file
SERVER='tulis-nama-sebagai-identifikasi-nama-server'

# Name of user on home directory | type your user directory at /home/user
USER='tulis-nama-folder-user'

# Gdrive folder target
REMOTE_NAME='nama-setingan-semote-di-rclone'
GDRIVE_FOLDER='nama-folder-di-google-drive'

# Settings for database connection
MYSQL_USER='nama-user-db'
MYSQL_PASSWORD='password-db'

# Temporary directory for backup
DIR11="/tmp/000-temp-backup-all-file-$USER"
# Directory source will zip
DIR21="/home/$USER/0-temp-backup-database-$USER"
DIR22="/home/$USER"

# Kind of backup
KIND1='only_databases'
KIND2='website_and_databases'



#------------------Section 2 = Do not change this until you know what it is------------------

# Path file location
MYSQL=/usr/bin/mysql
MYSQLDUMP=/usr/bin/mysqldump
# DRIVE=/usr/sbin/drive

# Filename by date and time
DATE=`date +"%Y%m%d-%H%M%S"`;

# Mysql database exclude to dump
DBEXCLUDE='Database|information_schema|performance_schema'



#------------------Section 3 = Execution------------------

# Creating temporary directory
echo "####################### Creating temporary directory"
mkdir -p "$DIR11"
mkdir -p "$DIR21"
echo "----------------------> Temporary directory Created"
echo ""

# Dumping database
echo "####################### Dumping database"
databases=`$MYSQL --user=$MYSQL_USER -p$MYSQL_PASSWORD -e "SHOW DATABASES LIKE '$USER\_%';" | grep -Ev "($DBEXCLUDE)"`

for db in $databases; do
  $MYSQLDUMP --force --opt --user=$MYSQL_USER -p$MYSQL_PASSWORD --databases $db | gzip > "$DIR21/$db.gz"
done
echo "----------------------> Database dumped"
echo ""


# Compressing the directory
echo "####################### Compressing the directory"
tar -zcf "$DIR11/$SERVER-$USER-$DATE-$KIND1.tgz" $DIR21
echo "----------------------> Database directory backup compressed"
tar -zcf "$DIR11/$SERVER-$USER-$DATE-$KIND2.tgz" $DIR22
echo "----------------------> All directory compressed"
echo ""


# Uploading to google drive
echo "####################### Uploading to Google Drive"
files=($(ls $DIR11/*.tgz))
for i in "${files[@]}"
do
   # $DRIVE upload --parent $GDRIVE_FOLDER --file $i
   rclone copy $i $REMOTE_NAME:$GDRIVE_FOLDER
done
echo "----------------------> Finished uploading to Google Drive"
echo ""


# Deleting temporary directory
echo "####################### Deleting temporary directory"
find $DIR11 -type d -print0 | xargs -0 rm -rf "{}"
find $DIR21 -type d -print0 | xargs -0 rm -rf "{}"
echo "----------------------> Temporary directory deleted"
echo ""


echo "#######################"
echo "----------------------> ALL DONE"
