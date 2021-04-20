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
}