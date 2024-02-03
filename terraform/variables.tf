variable "aws_profile" {
  description = "value of the AWS profile to use"
  type        = string
}
variable "aws_region" {
  description = "The AWS region to deploy to"
  type        = string
}

variable "service_name" {
  description = "The name of the service"
  type        = string
}

variable "tags" {
  description = "A map of tags to add to all resources"
  type        = map(string)
  default     = {}
}

variable "DB_USER" {
  description = "The username for the database"
  type        = string
}

variable "DB_PASS" {
  description = "The password for the database"
  type        = string
}
