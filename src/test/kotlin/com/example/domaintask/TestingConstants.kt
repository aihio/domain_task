package com.example.domaintask

object TestingConstants {
    const val XML_WITH_PRICES =
        """<?xml version="1.0" encoding="utf-8"?>
<ApiResponse Status="OK" xmlns="http://api.namecheap.com/xml.response">
	<CommandResponse Type="namecheap.users.getPricing">
		<UserGetPricingResult>
			<ProductType Name="domains">
				<ProductCategory Name="register">
					<Product Name="test">
						<Price Duration="1" DurationType="YEAR" Price="55.98" Currency="USD"/>
					</Product>
				</ProductCategory>
			</ProductType>
		</UserGetPricingResult>
	</CommandResponse>
</ApiResponse>"""

    const val RAW_JSON_WITH_NAME_AND_DATE = """
            {
                 "WhoisRecord": {
                     "registrarName": "ICANN",
                      "registryData": {
                        "expiresDate": "2010-08-30T04:00:00Z"
                        }
                 }
            }
        """
    const val CONTENT_TYPE = "Content-Type"
    const val APPLICATION_JSON = "application/json"
    const val XML_WITH_USERNAME_ERROR =
        """<?xml version="1.0" encoding="utf-8"?>
<ApiResponse Status="ERROR" xmlns="http://api.namecheap.com/xml.response">
    <Errors>
        <Error Number="1019103">Username does not exist</Error>
    </Errors>
</ApiResponse>"""
}

