````markdown
# Pricing & Billing Use Case Diagram (Simplified)

Combined view of the pricing/billing use cases:

- UC-PRC-01: Calculate Trip Cost
- UC-PRC-02: View Billing Info
- UC-PRC-03: View Membership & Fees

## Diagram

```plantuml
@startuml
left to right direction

actor "Rider" as rider
actor "Guest" as guest
actor "System" as system

rectangle "QwikRide System" {
	usecase "Calculate Trip Cost" as UC_PRC_01
	usecase "View Billing Info" as UC_PRC_02
	usecase "View Membership & Fees" as UC_PRC_03
}

system --> UC_PRC_01
rider --> UC_PRC_02
guest --> UC_PRC_03

note right of UC_PRC_01
Auto-triggered on bike return
(trip completion)
end note

@enduml
```

Minimal notes:

- UC-PRC-01 is automatic when the rider returns a bike (trip completion event handled by pricing).
````
