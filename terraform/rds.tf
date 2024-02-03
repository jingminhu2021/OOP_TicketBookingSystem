#create a security group for RDS Database Instance
resource "aws_security_group" "rds_sg" {
  name = "rds_sg"
  ingress {
    from_port   = 3306
    to_port     = 3306
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

#create a RDS Database Instance
resource "aws_db_instance" "myinstance" {
  engine                     = "mysql"
  identifier                 = "myrdsinstance"
  allocated_storage          = 10
  max_allocated_storage      = 20
  engine_version             = "8.0.35"
  instance_class             = "db.t2.micro"
  username                   = var.DB_USER
  password                   = var.DB_PASS
  parameter_group_name       = "default.mysql8.0"
  vpc_security_group_ids     = ["${aws_security_group.rds_sg.id}"]
  skip_final_snapshot        = true
  publicly_accessible        = true
  auto_minor_version_upgrade = false
  ca_cert_identifier         = "rds-ca-rsa2048-g1"
}
