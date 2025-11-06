@startuml
title PRC — End-to-End Sequence (Return → Bill → Pay)

actor Rider
boundary "Station (Dock/App)" as Station
control "PRC System" as PRC
database "Ledger Store" as Ledger
database "Plan Store" as Plans
boundary "Billing UI" as UI
participant "Payment Gateway" as Pay
participant "Notification Service" as Notify

== Trip completion ==
Rider -> Station : Return bike
Station -> PRC : TripCompleted(startTime,endTime,\nstartStationId,endStationId,bikeId,isEbike,riderId)

PRC -> Plans : getActivePlanVersion(effectiveDate)
Plans --> PRC : planVersionId, rates

group Pricing (deterministic)
  PRC -> PRC : Strategy.selectPlan(planVersionId)\nCoR.applyRules(base→perMin→eBike)
end

PRC -> PRC : buildTripSummary(duration,charges[],total)
PRC -> Ledger : createLedgerEntry(planVersionId,\ncharges[],total,status=pending)
Ledger --> PRC : ledgerEntryId

PRC -> Notify : sendTripSummary(riderId,summary)
Notify --> Rider : Trip summary delivered

== Rider views billing ==
opt Rider chooses "Billing History"
  Rider -> UI : OpenBillingHistory()
  UI -> PRC : getBilling(riderId, [dateRange])
  PRC -> Ledger : queryEntriesAndSummaries(riderId,[dateRange])
  Ledger --> PRC : entries[], totals, statuses
  PRC --> UI : BillingHistory[]
end

== Rider pays balance ==
opt Rider clicks "Pay Now"
  Rider -> UI : payBalance(amountDue, method)
  UI -> PRC : createPaymentIntent(riderId, amountDue, method)

  alt Balance > 0
    PRC -> Pay : createAndConfirmPayment(intentPayload)
    Pay --> PRC : paymentResult(status="CONFIRMED", transactionId)

    alt Payment confirmed
      PRC -> Ledger : markEntriesPaid(riderId, transactionId)\n+ storeReceiptLink()
      Ledger --> PRC : ok
      PRC -> Notify : sendReceipt(riderId, receiptLink)
      Notify --> Rider : Receipt delivered
    else Payment failed
      PRC -> UI : paymentStatus("FAILED", reason)
    end
  else No balance due
    PRC -> UI : paymentStatus("NONE_DUE")
  end
end
@enduml
