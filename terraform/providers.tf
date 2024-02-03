# Configure the AWS Provider
provider "aws" {
  region = var.aws_region
  # shared_credentials_files =  "~\.aws\credentials"
  profile = var.aws_profile
  default_tags {
    tags = var.tags
  }
}
