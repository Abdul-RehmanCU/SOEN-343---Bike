@startuml
title PRC — Combined Activity (Return → Bill → Pay / Export / Dispute)

skinparam ArrowColor #555
skinparam ActivityBackgroundColor #F9FBFF
skinparam ActivityBorderColor #88A
skinparam linetype ortho
skinparam shadowing false

start

partition "Trip Service (Stations/Apps)" {
  :Bike returned at dock;
  :Emit TripCompleted event;
}

partition "PRC System" {
  :Accept TripCompleted event;
  :Fetch trip facts (start/end,\nstations, bikeId, isEbike, riderId);
  :Resolve effectiveDate & membership;

  note right
  <<Strategy>>
  Select PricingPlanVersion (planVersionId)
  based on effectiveDate, city, membership
  end note

  :Select PricingPlanVersion (planVersionId);

  note right
  <<Chain of Responsibility>>
  Execute pricing handlers in order:
  1) BaseFeeHandler
  2) PerMinuteHandler
  3) EbikeSurchargeHandler
  (optional) Discount/Cap/Tax
  end note

  :Apply pricing rules deterministically;
  :Generate TripSummary;
  :Create immutable LedgerEntry\n(with planVersionId & breakdown);
  :Update Rider pending balance;
  :Send Trip Summary notification;
}

partition "Notification Service" {
  :Deliver trip summary;
}

' -------- Independent post-billing flows (can occur anytime) --------
fork
  ' View Billing (optional)
  partition Rider {
    :Open Billing History;
  }
  partition "PRC System" {
    :Retrieve ledger & trip summaries;
    :Render billing items\n(start/end, bike, stations, charges, total, status);
  }
fork again
  ' Settle Payment (optional)
  partition Rider {
    :Click "Pay now";
  }
  partition "PRC System" {
    if (Balance > 0?) then (yes)
      :Create PaymentIntent;
    else (no)
      :Show "No payment due";
    endif
  }
  partition "External Payment Gateway" {
    :Authorize / confirm payment;
  }
  partition "PRC System" {
    if (Payment confirmed?) then (yes)
      :Mark related ledger entries as PAID;
      :Generate receipt (PDF);
      :Send receipt notification;
    else (no)
      :Show payment failed status;
    endif
  }
  partition "Notification Service" {
    :Deliver receipt / status;
  }
fork again
  ' Export (optional)
  partition Admin {
    :Select export date range;
  }
  partition "PRC System" {
    :Collect matching ledger entries;
    :Generate CSV export;
    :Provide download;
  }
fork again
  ' Dispute (optional)
  partition Rider {
    :Submit dispute for a trip;
  }
  partition "PRC System" {
    :Create DisputeTicket;
  }
  partition Admin {
    :Review trip & station logs, evidence;
    if (Approved?) then (yes)
      :Approve adjustment;
    else (no)
      :Reject dispute;
    endif
  }
  partition "PRC System" {
    if (Approved?) then (yes)
      :Compute adjustment;
      :Post Adjustment LedgerEntry\n(immutable; original not altered);
      :Send dispute resolution (Approved);
    else (no)
      :Send dispute resolution (Rejected);
    endif
  }
  partition "Notification Service" {
    :Deliver resolution notice;
  }
end fork

stop
@enduml
