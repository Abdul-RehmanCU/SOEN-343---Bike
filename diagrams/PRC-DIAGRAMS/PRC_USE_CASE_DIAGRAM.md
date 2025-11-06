@startuml
title PRC — Pricing & Billing (Use Case Overview)

left to right direction
skinparam packageStyle rectangle
skinparam usecase {
  BorderColor #555
  BackgroundColor #EEF3FF
  ArrowColor #555
}

' === Actors ===
actor Rider
actor Guest
actor Admin
actor "Trip Service\n(Stations/Apps)" as TripSvc
actor "External Payment\nGateway" as Payment
actor "Notification\nService" as Notifier

' === System boundary ===
rectangle "PRC System" as PRC {
  (UC-PRC-01\nCalculate Trip Cost) as UC1
  (UC-PRC-02\nView Billing History) as UC2
  (UC-PRC-03\nView Membership & Fees) as UC3
  (UC-PRC-04\nSettle Payment) as UC4
  (UC-PRC-05\nManage Pricing Plans) as UC5
  (UC-PRC-06\nDownload Receipts & Ledger) as UC6
  (UC-PRC-07\nDispute Trip Charge) as UC7

  ' Reusable subflows (no stereotypes on nodes)
  (Generate Trip Summary) as GenSummary
  (Create Ledger Entry) as Ledger
  (Notify Rider) as NotifyUC
  (Create Adjustment Entry) as Adj
}

' === Primary associations ===
TripSvc -- UC1
Rider -- UC2
Rider -- UC4
Rider -- UC6
Rider -- UC7
Guest -- UC3
Admin -- UC5
Admin -- UC6
Admin -- UC7

' === Includes (shared internal behavior) ===
UC1 ..> GenSummary : <<include>>
UC1 ..> Ledger     : <<include>>
UC1 ..> NotifyUC   : <<include>>

UC4 ..> Ledger     : <<include>>
UC4 ..> NotifyUC   : <<include>>

UC7 ..> Adj        : <<include>>
UC7 ..> Ledger     : <<include>>
UC7 ..> NotifyUC   : <<include>>

' === External service collaborations ===
Payment -- UC4
Notifier -- NotifyUC

' === Notes ===
note right of UC1
Uses Strategy to select pricing plan
and Chain of Responsibility to apply rules
(base, per-minute, e-bike surcharge).
end note

note bottom of UC5
Versioned pricing (planVersionId) ensures
reproducible billing for historical trips.
end note
@enduml
